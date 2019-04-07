package com.slimgears.util.guice;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

public abstract class AbstractScope implements Scope {
    @Override
    public <T> Provider<T> scope(Key<T> key, Provider<T> unscopedProvider) {
        return () -> provide(key, unscopedProvider);
    }

    protected abstract <T> T provide(Key<T> key, Provider<T> unscopedProvider);
}
