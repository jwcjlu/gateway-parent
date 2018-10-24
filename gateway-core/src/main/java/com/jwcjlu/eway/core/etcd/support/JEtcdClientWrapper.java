package com.jwcjlu.gateway.core.etcd.support;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.Watch;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.data.KeyValue;
import com.coreos.jetcd.kv.GetResponse;
import com.coreos.jetcd.options.GetOption;
import com.coreos.jetcd.options.PutOption;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.jwcjlu.gateway.common.constant.Constants;
import com.jwcjlu.gateway.common.util.EtcdUtil;
import com.jwcjlu.gateway.common.util.NamedThreadFactory;
import com.jwcjlu.gateway.core.etcd.StateListener;
import com.jwcjlu.gateway.core.etcd.option.OptionUtil;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.util.RoundRobinLoadBalancerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

import static java.util.stream.Collectors.toList;

public class JEtcdClientWrapper {
    private Logger logger = LoggerFactory.getLogger(JEtcdClientWrapper.class);

    private final String endPoints;
    private Client client;
    private volatile boolean started = false;
    private volatile boolean connectState = false;
    private ScheduledFuture future;
    private ScheduledExecutorService reconnectNotify;
    private AtomicReference<ManagedChannel> channel;

    private ConnectionStateListener connectionStateListener;

    private long expirePeriod;

    private ListenableFutureTask<Client> listenableFutureTask;

    public JEtcdClientWrapper(String endPoints) {
        this.endPoints = endPoints;
        this.expirePeriod = (Constants.ETCD_DEFAULT_KEEPALIVE_TIMEOUT) / 1000;
        this.channel = new AtomicReference<>();
        this.listenableFutureTask = ListenableFutureTask.create(() -> {
            return Client.builder()
                .loadBalancerFactory(RoundRobinLoadBalancerFactory.getInstance())
                .endpoints(endPoints(endPoints)).build();
        });
        this.reconnectNotify = Executors.newScheduledThreadPool(1,
            new NamedThreadFactory("reconnectNotify"));
    }

    public Client getClient() {
        return client;
    }

    /**
     * try to get current connected channel.
     *
     * @return connected channel.
     */
    public ManagedChannel getChannel() {
        if (channel.get() == null || (channel.get().isShutdown() || channel.get().isTerminated())) {
            channel.set(newChannel(client));
        }
        return channel.get();
    }

    /**
     * find direct children directory, excluding path self,
     * Never return null.
     *
     * @param path the path to be found direct children.
     * @return direct children directory, contains zero element
     * list if children directory not exists.
     */
    public List<String> getChildren(String path) {
        for (; ; ) {
            try {
                int len = path.length();
                return client.getKVClient()
                    .get(ByteSequence.fromString(path),
                        GetOption.newBuilder().withPrefix(ByteSequence.fromString(path)).build()).get()
                    .getKvs().stream().parallel()
                    .map(pair -> EtcdUtil.getChildPath(path,pair.getKey().toStringUtf8()))
                    .collect(toList());
            } catch (Exception e) {
                Status status = Status.fromThrowable(e);
                if (status != null && status.getCode() == Status.Code.UNKNOWN) {
                    throw new IllegalStateException("failed to get children by path '" + path + "'", e);
                }
                if (OptionUtil.isRecoverable(status)) {
                    LockSupport.parkNanos(this, TimeUnit.MILLISECONDS.toNanos(50));
                    continue;
                }
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    public boolean isConnected() {
        return !(getChannel().isShutdown()
            || getChannel().isTerminated());
    }

    public long createLease(long second) {
        for (; ; ) {
            try {
                return client.getLeaseClient()
                    .grant(second).get().getID();
            } catch (Exception e) {
                Status status = Status.fromThrowable(e);
                if (status != null && status.getCode() == Status.Code.UNKNOWN) {
                    throw new IllegalStateException("failed to create lease grant second '" + second + "'", e);
                }
                if (OptionUtil.isRecoverable(status)) {
                    LockSupport.parkNanos(this, TimeUnit.MILLISECONDS.toNanos(50));
                    continue;
                }
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    public void revokeLease(long lease) {
        for (; ; ) {
            try {
                client.getLeaseClient()
                    .revoke(lease).get();
                break;
            } catch (Exception e) {
                Status status = Status.fromThrowable(e);
                if (status != null && status.getCode() == Status.Code.NOT_FOUND) {
                    break;
                }
                if (status != null && status.getCode() == Status.Code.UNKNOWN) {
                    throw new IllegalStateException("failed to revoke lease '" + lease + "'", e);
                }
                if (OptionUtil.isRecoverable(status)) {
                    LockSupport.parkNanos(this, TimeUnit.MILLISECONDS.toNanos(50));
                    continue;
                }
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    public long createLease(long ttl, long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException {

        if (timeout <= 0) {
            return createLease(ttl);
        }

        return client.getLeaseClient()
            .grant(ttl)
            .get(timeout, unit).getID();
    }


    /**
     * try to check if path exists.
     */
    public boolean checkExists(String path) {
        for (; ; ) {
            try {
                return client.getKVClient()
                    .get(ByteSequence.fromString(path), GetOption.newBuilder().withCountOnly(true).build())
                    .get().getCount() > 0;
            } catch (Exception e) {
                Status status = Status.fromThrowable(e);
                if (status != null && status.getCode() == Status.Code.UNKNOWN) {
                    throw new IllegalStateException("failed to check exists by path '" + path + "'", e);
                }
                if (OptionUtil.isRecoverable(status)) {
                    LockSupport.parkNanos(this, TimeUnit.MILLISECONDS.toNanos(50));
                    continue;
                }
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    /**
     * only internal use only, maybe change in the future
     */
    protected Long find(String path) {
        for (; ; ) {
            try {
                return client.getKVClient()
                    .get(ByteSequence.fromString(path)).get()
                    .getKvs().stream()
                    .mapToLong(keyValue -> Long.valueOf(keyValue.getValue().toStringUtf8()))
                    .findFirst().getAsLong();
            } catch (Exception e) {
                Status status = Status.fromThrowable(e);
                if (status != null && status.getCode() == Status.Code.UNKNOWN) {
                    throw new IllegalStateException("failed to find path by path '" + path + "'", e);
                }
                if (OptionUtil.isRecoverable(status)) {
                    LockSupport.parkNanos(this, TimeUnit.MILLISECONDS.toNanos(50));
                    continue;
                }
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    public void createPersistent(String path) {
        for (; ; ) {
            try {
                client.getKVClient()
                    .put(ByteSequence.fromString(path),
                        ByteSequence.fromString(String.valueOf(path.hashCode()))).get();
                break;
            } catch (Exception e) {
                Status status = Status.fromThrowable(e);
                if (status != null && status.getCode() == Status.Code.UNKNOWN) {
                    throw new IllegalStateException("failed to create persistent  by path '" + path + "'", e);
                }
                if (OptionUtil.isRecoverable(status)) {
                    LockSupport.parkNanos(this, TimeUnit.MILLISECONDS.toNanos(50));
                    continue;
                }
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    /**
     * create new ephemeral path save to etcd .
     * if node disconnect from etcd, it will be deleted
     * automatically by etcd when sessian timeout.
     *
     * @param path the path to be saved
     * @return the lease of current path.
     */
    public long createEphemeral(String path) {
        for (; ; ) {
            try {
                long lease = client.getLeaseClient().grant(expirePeriod).get().getID();
                keepAlive(lease);
                client.getKVClient()
                    .put(ByteSequence.fromString(path)
                        , ByteSequence.fromString(String.valueOf(lease))
                        , PutOption.newBuilder().withLeaseId(lease).build()).get();
                return lease;
            } catch (Exception e) {
                Status status = Status.fromThrowable(e);
                if (status != null && status.getCode() == Status.Code.UNKNOWN) {
                    throw new IllegalStateException("failed to create ephereral by path '" + path + "'", e);
                }
                if (OptionUtil.isRecoverable(status)) {
                    LockSupport.parkNanos(this, TimeUnit.MILLISECONDS.toNanos(50));
                    continue;
                }
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    public List<KeyValue> getData(String path) {
        for (int i = 0; i < 3; i++) {
            try {
                GetResponse getResponse = client.getKVClient().get(
                    ByteSequence.fromString(path),
                    GetOption.newBuilder().build()
                ).get();
                return getResponse.getKvs();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // easy for mock
    public void keepAlive(long lease) {
        client.getLeaseClient().keepAlive(lease);
    }

    public void delete(String path) {
        for (; ; ) {
            try {
                client.getKVClient()
                    .delete(ByteSequence.fromString(path)).get();
                break;
            } catch (Exception e) {
                Status status = Status.fromThrowable(e);
                if (status != null && status.getCode() == Status.Code.UNKNOWN) {
                    throw new IllegalStateException("failed to delete by path '" + path + "'", e);
                }
                if (OptionUtil.isRecoverable(status)) {
                    LockSupport.parkNanos(this, TimeUnit.MILLISECONDS.toNanos(50));
                    continue;
                }
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    public List<String> endPoints(String backupAddress) {
        String[] endpoints = backupAddress.split(Constants.COMMA_SEPARATOR);
        return Arrays.stream(endpoints)
            .map(address -> address.indexOf(Constants.HTTP_SUBFIX_KEY) > -1
                ? address
                : Constants.HTTP_KEY + address)
            .collect(toList());
    }

    /**
     * because jetcd's connection change callback not supported yet, we must
     * loop to test if connect or disconnect event happend or not. It will be changed
     * in the future if we found better choice.
     */
    public void start() {
        if (!started) {
            try {

                Thread connectThread = new Thread(listenableFutureTask);
                connectThread.setName("EwayEtcdClientConnector");
                connectThread.setDaemon(true);
                connectThread.start();
                this.client = listenableFutureTask.get(expirePeriod, TimeUnit.SECONDS);
                this.connectState = isConnected();
                this.started = true;
            } catch (Throwable t) {
                logger.error("Etcd3 server can not be connected in " + expirePeriod + " second! etcd3 address: " + endPoints(endPoints), t);
            }

            try {
                int retry = 3;
                this.future = reconnectNotify.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        boolean state = isConnected();
                        if (state != connectState) {
                            int notifyState = state ? StateListener.CONNECTED : StateListener.DISCONNECTED;
                            if (connectionStateListener != null) {
                                connectionStateListener.stateChanged(getClient(), notifyState);
                            }
                            connectState = state;
                        }
                    }
                }, retry, retry, TimeUnit.SECONDS);
            } catch (Throwable t) {
                logger.error("monitor reconnect status failed.", t);
            }
        }
    }

    protected void doClose() {
        try {
            if (started && future != null) {
                started = false;
                future.cancel(true);
                reconnectNotify.shutdownNow();
            }
        } catch (Exception e) {
            logger.warn("stop reconnect Notify failed.", e);
        }
        if (getClient() != null) getClient().close();
    }

    /**
     * try get client's shared channel, becase all fields is private on jetcd,
     * we must using it by reflect, in the future, jetcd may provider better tools.
     *
     * @param client get channel from current client
     * @return current connection channel
     */
    private ManagedChannel newChannel(Client client) {
        try {
            Field connectionField = client.getClass().getDeclaredField("connectionManager");
            if (!connectionField.isAccessible()) {
                connectionField.setAccessible(true);
            }
            Object connection = connectionField.get(client);
            Method channel = connection.getClass().getDeclaredMethod("getChannel");
            if (!channel.isAccessible()) {
                channel.setAccessible(true);
            }
            return (ManagedChannel) channel.invoke(connection);
        } catch (Exception e) {
            throw new RuntimeException("get connection channel failed from " + endPoints, e);
        }
    }

    public ConnectionStateListener getConnectionStateListener() {
        return connectionStateListener;
    }

    public void setConnectionStateListener(ConnectionStateListener connectionStateListener) {
        this.connectionStateListener = connectionStateListener;
    }

    public Watch getClientWatch() {
        return client.getWatchClient();
    }


    public interface ConnectionStateListener {
        /**
         * Called when there is a state change in the connection
         *
         * @param client   the client
         * @param newState the new state
         */
        public void stateChanged(Client client, int newState);
    }
}
