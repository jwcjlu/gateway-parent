package com.jwcjlu.gateway.core.etcd.support;

import com.coreos.jetcd.api.*;
import com.coreos.jetcd.common.exception.ClosedClientException;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.data.KeyValue;
import com.google.protobuf.ByteString;
import com.jwcjlu.gateway.common.constant.Constants;
import com.jwcjlu.gateway.common.util.NamedThreadFactory;
import com.jwcjlu.gateway.core.etcd.ChildListener;
import com.jwcjlu.gateway.core.etcd.DataListener;
import com.jwcjlu.gateway.core.etcd.Listener;
import com.jwcjlu.gateway.core.etcd.StateListener;
import com.jwcjlu.gateway.core.etcd.option.OptionUtil;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class JEtcdClient extends AbstractEtcdClient<JEtcdClient.EtcdWatcher> {
    private JEtcdClientWrapper clientWrapper;
    private ScheduledExecutorService reconnectSchedule;

    public JEtcdClient(String endPoints) {
        try {
            clientWrapper = new JEtcdClientWrapper(endPoints);
            clientWrapper.setConnectionStateListener((client, state) -> {
                if (state == StateListener.CONNECTED) {
                    JEtcdClient.this.stateChanged(StateListener.CONNECTED);
                } else if (state == StateListener.DISCONNECTED) {
                    JEtcdClient.this.stateChanged(StateListener.DISCONNECTED);
                }
            });
            reconnectSchedule = Executors.newScheduledThreadPool(1,
                new NamedThreadFactory("auto-reconnect"));
            clientWrapper.start();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void doCreatePersistent(String path) {
        clientWrapper.createPersistent(path);
    }

    @Override
    public long doCreateEphemeral(String path) {
        return clientWrapper.createEphemeral(path);
    }

    @Override
    public boolean checkExists(String path) {
        return clientWrapper.checkExists(path);
    }

    @Override
    public EtcdWatcher createChildWatcherListener(String path, ChildListener listener) {
        return new ChildEtcdWatcher(listener);
    }

    @Override
    public List<String> addChildWatcherListener(String path, EtcdWatcher etcdWatcher) {
        return etcdWatcher.forPath(path);
    }

    @Override
    public void removeChildWatcherListener(String path, EtcdWatcher etcdWatcher) {
        etcdWatcher.unwatch();
    }


    @Override
    public EtcdWatcher createDataWatcherListener(String path, DataListener listener) {
        return new DataEtcdWatcher(listener);
    }

    @Override
    public String addDataWatcherListener(String path, EtcdWatcher etcdWatcher) {
        List<String> datas = etcdWatcher.forPath(path);
        return Objects.isNull(datas) ? null : datas.get(0);

    }

    @Override
    public void removeDataWatcherListener(String path, EtcdWatcher etcdWatcher) {
        etcdWatcher.unwatch();
    }


    @Override
    public List<String> getChildren(String path) {
        return clientWrapper.getChildren(path);
    }

    @Override
    public boolean isConnected() {
        return clientWrapper.isConnected();
    }

    @Override
    public long createLease(long second) {
        return clientWrapper.createLease(second);
    }

    @Override
    public long createLease(long ttl, long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException {
        return clientWrapper.createLease(ttl, timeout, unit);
    }

    @Override
    public List<com.coreos.jetcd.data.KeyValue> getKey(String key) {
        return clientWrapper.getData(key);
    }

    @Override
    public void delete(String path) {
        clientWrapper.delete(path);
    }

    @Override
    public void revokeLease(long lease) {
        clientWrapper.revokeLease(lease);
    }

    @Override
    public boolean isExist(String path) {
        return checkExists(path);
    }


    @Override
    public void doClose() {
        try {
            reconnectSchedule.shutdownNow();
        } catch (Exception e) {

        } finally {
            clientWrapper.doClose();
        }
    }

    public abstract class EtcdWatcher implements StreamObserver<WatchResponse> {

        protected Listener listener;
        protected WatchGrpc.WatchStub watchStub;
        protected StreamObserver<WatchRequest> watchRequest;
        protected long watchId;
        protected String path;
        protected Throwable throwable;
        private AtomicInteger retry = new AtomicInteger();

        public EtcdWatcher(Listener listener) {
            this.listener = listener;
        }

        @Override
        public void onError(Throwable e) {
            tryReconnect(e);
        }

        public void unwatch() {

            // prevents grpc on sending watchResponse to a closed watch client.
            if (!isConnected()) {
                return;
            }

            try {
                this.listener = null;
                if (watchRequest != null) {
                    WatchCancelRequest watchCancelRequest =
                        WatchCancelRequest.newBuilder().setWatchId(watchId).build();
                    WatchRequest cancelRequest = WatchRequest.newBuilder()
                        .setCancelRequest(watchCancelRequest).build();
                    this.watchRequest.onNext(cancelRequest);
                }
            } catch (Exception ignored) {
                // ignore
            }
        }

        public List<String> forPath(String path) {

            if (!isConnected()) {
                throw new ClosedClientException("watch client has been closed, path '" + path + "'");
            }

            if (this.path != null) {
                if (this.path.equals(path)) {
                    return clientWrapper.getChildren(path);
                }
                unwatch();
            }

            this.watchStub = WatchGrpc.newStub(clientWrapper.getChannel());
            this.watchRequest = watchStub.watch(this);
            this.path = path;
            this.watchRequest.onNext(nextRequest());

            return clientWrapper.getChildren(path);
        }


        public void tryReconnect(Throwable e) {

            this.throwable = e;

            logger.error("watcher client has error occurred, current path '" + path + "'", e);

            // prevents grpc on sending error to a closed watch client.
            if (!isConnected()) {
                return;
            }

            Status status = Status.fromThrowable(e);
            // system may be recover later, current connect won't be lost
            if (OptionUtil.isHaltError(status) || OptionUtil.isNoLeaderError(status)) {
                this.closeWatchRequest();
                return;
            }
            // reconnect with a delay; avoiding immediate retry on a long connection downtime.
            reconnectSchedule.schedule(this::reconnect, 500, TimeUnit.MILLISECONDS);
        }

        protected synchronized void reconnect() {
            this.closeWatchRequest();
            this.recreateWatchRequest();
        }

        protected void recreateWatchRequest() {
            if (watchRequest == null) {
                this.watchStub = WatchGrpc.newStub(clientWrapper.getChannel());
                this.watchRequest = watchStub.watch(this);
            }
            this.watchRequest.onNext(nextRequest());
            this.throwable = null;
            logger.warn("watch client retried connect for path '" + path + "', connection status : " + isConnected());
        }

        protected void closeWatchRequest() {
            if (this.watchRequest == null) {
                return;
            }
            this.watchRequest.onCompleted();
            this.watchRequest = null;
        }

        protected abstract WatchRequest nextRequest();

        @Override
        public void onCompleted() {
            // do not touch this method, if you want terminate this stream.
        }
    }

    /**
     *
     */
    public class ChildEtcdWatcher extends JEtcdClient.EtcdWatcher {
        public ChildEtcdWatcher(Listener listener) {
            super(listener);
        }

        private void handlerChildChange(Event event) {
            com.coreos.jetcd.api.KeyValue keyValue = event.getKv();
            String key = keyValue.getKey().toStringUtf8();

            int index = path.length(), count = 0;
            if (key.length() >= index) {
                for (; (index = key.indexOf(Constants.PATH_SEPARATOR, index)) != -1; ++index) {
                    if (count++ > 1) break;
                }
            }

            // if current path changed or direct path changed
            if (path.equals(key) || count == 1) {
                // May be optimized in the future.
                ((ChildListener) listener).childChanged(path, clientWrapper.getChildren(path));
            }
        }


        @Override
        public void onNext(WatchResponse response) {

            // prevents grpc on sending watchResponse to a closed watch client.
            if (!isConnected()) {
                return;
            }
            watchId = response.getWatchId();
            if (listener != null) {
                Iterator<Event> iterator = response.getEventsList().iterator();
                while (iterator.hasNext()) {
                    Event event = iterator.next();
                    switch (event.getType()) {
                        case PUT: {
                            handlerChildChange(event);
                            break;
                        }
                        case DELETE: {
                            handlerChildChange(event);
                            break;
                        }
                        default:
                            break;
                    }
                }
            }
        }

        /**
         * create new watching request for current path.
         */
        protected WatchRequest nextRequest() {

            WatchCreateRequest.Builder builder = WatchCreateRequest.newBuilder()
                .setKey(ByteString.copyFromUtf8(path))
                .setRangeEnd(
                    ByteString.copyFrom(
                        OptionUtil.prefixEndOf(ByteSequence.fromString(path)).getBytes()))
                .setProgressNotify(true);

            return WatchRequest.newBuilder().setCreateRequest(builder).build();
        }

        @Override
        public List<String> forPath(String path) {
            List<String> childers = super.forPath(path);
            ((ChildListener) listener).childChanged(path, childers);
            return childers;
        }

    }

    /**
     * Data content changes watch
     */
    public class DataEtcdWatcher extends JEtcdClient.EtcdWatcher {
        public DataEtcdWatcher(Listener listener) {
            super(listener);
        }

        @Override
        public void onNext(WatchResponse response) {

            // prevents grpc on sending watchResponse to a closed watch client.
            if (!isConnected()) {
                return;
            }
            watchId = response.getWatchId();
            if (listener != null) {
                Iterator<Event> iterator = response.getEventsList().iterator();
                while (iterator.hasNext()) {
                    Event event = iterator.next();
                    switch (event.getType()) {
                        case PUT: {
                            String key = event.getKv().getKey().toStringUtf8();
                            String value = event.getKv().getValue().toStringUtf8();
                            ((DataListener) (listener)).handleDataChange(key, value);
                            break;
                        }
                        case DELETE: {
                            String key = event.getKv().getKey().toStringUtf8();
                            String value = event.getKv().getValue().toStringUtf8();
                            ((DataListener) (listener)).handleDataChange(key, value);
                            break;
                        }
                        default:
                            break;
                    }
                }
            }
        }

        /**
         * create new watching request for current path.
         */
        protected WatchRequest nextRequest() {
            WatchCreateRequest.Builder builder = WatchCreateRequest.newBuilder()
                .setKey(ByteString.copyFromUtf8(path))
                .setProgressNotify(true);
            return WatchRequest.newBuilder().setCreateRequest(builder).build();
        }

        @Override
        public List<String> forPath(String path) {

            if (!isConnected()) {
                throw new ClosedClientException("watch client has been closed, path '" + path + "'");
            }
            if (this.path != null) {
                if (this.path.equals(path)) {
                    return getOriginValue();
                }
                unwatch();
            }
            this.watchStub = WatchGrpc.newStub(clientWrapper.getChannel());
            this.watchRequest = watchStub.watch(this);
            this.path = path;
            this.watchRequest.onNext(nextRequest());
            List<String> data = getOriginValue();
            if (CollectionUtils.isNotEmpty(data)) {
                ((DataListener) listener).handleDataChange(path, data.get(0));
            }
            return data;
        }

        private List<String> getOriginValue() {
            List<KeyValue> keyValues = clientWrapper.getData(path);
            if (Objects.isNull(keyValues) || keyValues.size() < 1) {
                return null;
            }
            return Arrays.asList(keyValues.get(0).getValue().toStringUtf8());
        }

    }
}
