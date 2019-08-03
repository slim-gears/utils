package com.slimgears.util.guice;

import com.google.common.reflect.TypeToken;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.slimgears.util.generic.ServiceResolver;

import java.util.function.Predicate;

@SuppressWarnings("UnstableApiUsage")
public class GuiceServiceResolver implements ServiceResolver {
    private final Injector injector;
    private final Predicate<? super Key<?>> resolvePredicate;

    public static ServiceResolver forInjector(Injector injector) {
        return new GuiceServiceResolver(injector, key -> true);
    }

    public static ServiceResolver forInjector(Injector injector, Predicate<? super Key<?>> predicate) {
        return new GuiceServiceResolver(injector, predicate);
    }

    public static <T> Key<T> toKey(TypeToken<T> token) {
        return Key.get(toTypeLiteral(token));
    }

    @SuppressWarnings("unchecked")
    public static <T> TypeLiteral<T> toTypeLiteral(TypeToken<T> token) {
        return (TypeLiteral<T>)TypeLiteral.get(token.getType());
    }

    private GuiceServiceResolver(Injector injector, Predicate<? super Key<?>> resolvePredicate) {
        this.injector = injector;
        this.resolvePredicate = resolvePredicate;
    }

    @Override
    public <T> T resolve(TypeToken<T> token) {
        return injector.getInstance(toKey(token));
    }

    @Override
    public boolean canResolve(TypeToken<?> token) {
        return resolvePredicate.test(toKey(token));
    }
}
