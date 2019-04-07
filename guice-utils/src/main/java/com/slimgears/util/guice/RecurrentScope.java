package com.slimgears.util.guice;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.slimgears.util.generic.RecurrentThreadLocal;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class RecurrentScope extends AbstractScope {
    private final RecurrentThreadLocal<Map<Key, Object>> objects = new RecurrentThreadLocal<>(HashMap::new);

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T provide(final Key<T> key, final Provider<T> provider) {
        Map<Key, Object> storage = objects.acquire();
        try {
            return (T)storage.computeIfAbsent(key, k -> provider.get());
        } finally {
            objects.release();
        }
    }

    public static void install(Binder binder, Class<? extends Annotation> scopeAnnotation) {
        RecurrentScope scope = new RecurrentScope();
        binder.bindScope(scopeAnnotation, scope);
    }

    public static void install(Binder binder) {
        install(binder, InjectionChainScoped.class);
    }
}
