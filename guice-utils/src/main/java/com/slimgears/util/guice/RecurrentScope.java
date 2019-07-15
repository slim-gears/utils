package com.slimgears.util.guice;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.slimgears.util.generic.RecurrentThreadLocal;

import java.lang.annotation.Annotation;

@SuppressWarnings("WeakerAccess")
public class RecurrentScope extends AbstractScope {
    private final RecurrentThreadLocal<ObjectStorage> objects = RecurrentThreadLocal.of(ObjectStorage::new);

    @Override
    protected <T> T provide(final Key<T> key, final Provider<T> provider) {
        ObjectStorage storage = objects.acquire();
        try {
            return storage.get(key, provider);
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
