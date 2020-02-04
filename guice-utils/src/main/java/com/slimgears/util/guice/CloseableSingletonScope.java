package com.slimgears.util.guice;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.slimgears.util.stream.Optionals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Stack;

public class CloseableSingletonScope extends AbstractScope implements AutoCloseable {
    private final Logger log = LoggerFactory.getLogger(CloseableSingletonScope.class);
    private final ObjectStorage objectStorage = new ObjectStorage();
    private final Stack<AutoCloseable> closeables = new Stack<>();

    public static void install(Binder binder) {
        CloseableSingletonScope scope = new CloseableSingletonScope();
        binder.bind(CloseableSingletonScope.class).toInstance(scope);
        binder.bindScope(CloseableSingleton.class, scope);
    }

    private CloseableSingletonScope() {
        objectStorage.subscribe(new ObjectStorage.InstantiationListener() {
            @Override
            public <T> void onInstantiated(Key<T> key, T instance) {
                Optional.ofNullable(instance)
                        .flatMap(Optionals.ofType(AutoCloseable.class))
                        .ifPresent(closeables::push);
            }
        });
    }

    @Override
    protected <T> T provide(Key<T> key, Provider<T> unscopedProvider) {
        return objectStorage.get(key, unscopedProvider);
    }

    @Override
    public void close() {
        while (!closeables.empty()) {
            AutoCloseable closeable = closeables.pop();
            try {
                closeable.close();
            } catch (Exception e) {
                log.warn("Exception occurred when trying to close {}: ", closeable.getClass().getSimpleName(), e);
            }
        }
    }
}
