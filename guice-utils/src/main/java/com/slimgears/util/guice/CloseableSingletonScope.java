package com.slimgears.util.guice;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Provider;

public class CloseableSingletonScope extends AbstractScope implements AutoCloseable {
    private final ObjectStorage objectStorage = new ObjectStorage();

    public static void install(Binder binder) {
        CloseableSingletonScope scope = new CloseableSingletonScope();
        binder.bind(CloseableSingletonScope.class).toInstance(scope);
        binder.bindScope(CloseableSingleton.class, scope);
    }

    private CloseableSingletonScope() {
    }

    @Override
    protected <T> T provide(Key<T> key, Provider<T> unscopedProvider) {
        return objectStorage.get(key, unscopedProvider);
    }

    @Override
    public void close() {
        objectStorage.close();
    }
}
