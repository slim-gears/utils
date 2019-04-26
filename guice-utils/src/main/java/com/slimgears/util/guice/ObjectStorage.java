package com.slimgears.util.guice;

import com.google.inject.Key;
import com.google.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

@SuppressWarnings("WeakerAccess")
class ObjectStorage implements AutoCloseable {
    private final static Logger LOG = LoggerFactory.getLogger(ObjectStorage.class);
    private final Map<Key<?>, Object> objectMap = new HashMap<>();
    private final Stack<AutoCloseable> closeables = new Stack<>();
    private final List<InstantiationListener> listeners = new CopyOnWriteArrayList<>();

    interface InstantiationListener {
        <T> void onInstantiated(Key<T> key, T instance);
    }

    @SuppressWarnings("unchecked")
    public synchronized <T> T get(Key<T> key, Provider<T> provider) {
        if (objectMap.containsKey(key)) {
            return (T) objectMap.get(key);
        }

        T obj = provider.get();
        objectMap.put(key, obj);

        Optional
                .of(listeners)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .forEach(l -> l.onInstantiated(key, obj));

        if (obj instanceof AutoCloseable) {
            closeables.push((AutoCloseable)obj);
        }

        return obj;
    }

    public boolean contains(Key key) {
        return objectMap.containsKey(key);
    }

    @Override
    public void close() {
        while (!closeables.isEmpty()) {
            AutoCloseable closeable = closeables.pop();
            try {
                closeable.close();
            } catch (Exception e) {
                LOG.error("Error occurred when closing " + closeable.getClass(), e);
            }
        }
    }

    public void subscribe(InstantiationListener listener) {
        listeners.add(listener);
    }
}
