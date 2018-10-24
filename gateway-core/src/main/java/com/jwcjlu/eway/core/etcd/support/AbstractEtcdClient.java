package com.jwcjlu.gateway.core.etcd.support;

import com.jwcjlu.gateway.common.concurrent.ConcurrentHashSet;
import com.jwcjlu.gateway.common.constant.Constants;
import com.jwcjlu.gateway.core.etcd.ChildListener;
import com.jwcjlu.gateway.core.etcd.DataListener;
import com.jwcjlu.gateway.core.etcd.EtcdClient;
import com.jwcjlu.gateway.core.etcd.StateListener;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractEtcdClient<WatcherListener> implements EtcdClient {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractEtcdClient.class);
    private final Set<StateListener> stateListeners = new ConcurrentHashSet<>();
    private final ConcurrentMap<String, ConcurrentMap<ChildListener, WatcherListener>> childListeners = new ConcurrentHashMap<String, ConcurrentMap<ChildListener, WatcherListener>>();
    private final ConcurrentMap<String, ConcurrentMap<DataListener, WatcherListener>> dataListeners = new ConcurrentHashMap<String, ConcurrentMap<DataListener, WatcherListener>>();

    private volatile boolean closed = false;
    private List<String> categroies = Arrays.asList("/gateway");

    protected String fixNamespace(String path) {
        if (StringUtils.isEmpty(path)) {
            throw new IllegalArgumentException("path is required, actual null or ''");
        }
        return (path.charAt(0) != '/') ? (Constants.PATH_SEPARATOR + path) : path;
    }

    @Override
    public void create(String path) {
        String fixedPath = fixNamespace(path);
        createParentIfAbsent(fixedPath);
        doCreatePersistent(fixedPath);
    }

    @Override
    public long createEphemeral(String path) {
        String fixedPath = fixNamespace(path);
        createParentIfAbsent(fixedPath);
        return doCreateEphemeral(path);
    }

    public void addStateListener(StateListener listener) {
        stateListeners.add(listener);
    }

    public void removeStateListener(StateListener listener) {
        stateListeners.remove(listener);
    }

    public Set<StateListener> getSessionListeners() {
        return stateListeners;
    }

    public List<String> addChildListener(String path, final ChildListener listener) {
        ConcurrentMap<ChildListener, WatcherListener> listeners = childListeners.get(path);
        if (listeners == null) {
            childListeners.putIfAbsent(path, new ConcurrentHashMap<ChildListener, WatcherListener>());
            listeners = childListeners.get(path);
        }
        WatcherListener targetListener = listeners.get(listener);
        if (targetListener == null) {
            listeners.putIfAbsent(listener, createChildWatcherListener(path, listener));
            targetListener = listeners.get(listener);
        }
        return addChildWatcherListener(path, targetListener);
    }

    public WatcherListener getChildListener(String path, ChildListener listener) {
        ConcurrentMap<ChildListener, WatcherListener> listeners = childListeners.get(path);
        if (listeners == null) {
            return null;
        }
        WatcherListener targetListener = listeners.get(listener);
        if (targetListener == null) {
            listeners.putIfAbsent(listener, createChildWatcherListener(path, listener));
            targetListener = listeners.get(listener);
        }
        return targetListener;
    }

    public void removeChildListener(String path, ChildListener listener) {
        ConcurrentMap<ChildListener, WatcherListener> listeners = childListeners.get(path);
        if (listeners != null) {
            WatcherListener targetListener = listeners.remove(listener);
            if (targetListener != null) {
                removeChildWatcherListener(path, targetListener);
            }
        }
    }


    //------------------------------------------------
    public String addDataListener(String path, final DataListener listener) {
        ConcurrentMap<DataListener, WatcherListener> listeners = dataListeners.get(path);
        if (listeners == null) {
            dataListeners.putIfAbsent(path, new ConcurrentHashMap<DataListener, WatcherListener>());
            listeners = dataListeners.get(path);
        }
        WatcherListener targetListener = listeners.get(listener);
        if (targetListener == null) {
            listeners.putIfAbsent(listener, createDataWatcherListener(path, listener));
            targetListener = listeners.get(listener);
        }
        return addDataWatcherListener(path, targetListener);
    }

    public WatcherListener getDataListener(String path, DataListener listener) {
        ConcurrentMap<DataListener, WatcherListener> listeners = dataListeners.get(path);
        if (listeners == null) {
            return null;
        }
        WatcherListener targetListener = listeners.get(listener);
        if (targetListener == null) {
            listeners.putIfAbsent(listener, createDataWatcherListener(path, listener));
            targetListener = listeners.get(listener);
        }
        return targetListener;
    }

    public void removeDataListener(String path, DataListener listener) {
        ConcurrentMap<DataListener, WatcherListener> listeners = dataListeners.get(path);
        if (listeners != null) {
            WatcherListener targetListener = listeners.remove(listener);
            if (targetListener != null) {
                removeDataWatcherListener(path, targetListener);
            }
        }
    }
    public void removeDataListener(String path) {
        ConcurrentMap<DataListener, WatcherListener> listeners = dataListeners.remove(path);

        if (listeners != null) {
            for(WatcherListener targetListener:listeners.values()){
                removeDataWatcherListener(path, targetListener);
            }

        }
    }
    public void removeChildListener(String path) {
        ConcurrentMap<ChildListener, WatcherListener> listeners = childListeners.remove(path);

        if (listeners != null) {
            for(WatcherListener targetListener:listeners.values()){
                removeChildWatcherListener(path, targetListener);
            }

        }
    }
    //-----------------------------------------------


    protected void stateChanged(int state) {
        for (StateListener sessionListener : getSessionListeners()) {
            sessionListener.stateChanged(state);
        }
    }

    protected void createParentIfAbsent(String fixedPath) {
        int i = fixedPath.lastIndexOf('/');
        if (i > 0) {
            String parentPath = fixedPath.substring(0, i);
            if (categroies.stream().anyMatch(c -> fixedPath.endsWith(c))) {
                if (!checkExists(parentPath)) {
                    this.doCreatePersistent(parentPath);
                }
            } else if (categroies.stream().anyMatch(c -> parentPath.endsWith(c))) {
                String grandfather = parentPath.substring(0, parentPath.lastIndexOf('/'));
                if (!checkExists(grandfather)) {
                    this.doCreatePersistent(grandfather);
                }
            }
        }
    }

    public void close() {
        if (closed) {
            return;
        }
        closed = true;
        try {
            doClose();
        } catch (Throwable t) {
            logger.warn(t.getMessage(), t);
        }
    }

    public abstract void doClose();

    public abstract void doCreatePersistent(String path);

    public abstract long doCreateEphemeral(String path);

    public abstract void delete(String path);

    public abstract boolean checkExists(String path);

    public abstract WatcherListener createChildWatcherListener(String path, ChildListener listener);

    public abstract List<String> addChildWatcherListener(String path, WatcherListener listener);

    public abstract void removeChildWatcherListener(String path, WatcherListener listener);

    public abstract WatcherListener createDataWatcherListener(String path, DataListener listener);

    public abstract String addDataWatcherListener(String path, WatcherListener listener);

    public abstract void removeDataWatcherListener(String path, WatcherListener listener);
}
