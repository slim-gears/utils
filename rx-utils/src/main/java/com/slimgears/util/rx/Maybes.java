package com.slimgears.util.rx;

import io.reactivex.Maybe;
import io.reactivex.MaybeTransformer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.Callable;

public class Maybes {
    public static <T> MaybeTransformer<T, T> defaultIfEmpty(Callable<T> defaultSupplier) {
        return src -> src
                .map(Maybe::just)
                .defaultIfEmpty(Maybe.fromCallable(defaultSupplier))
                .flatMap(m -> m);
    }

    public static <T> MaybeTransformer<T, T> orElse(Maybe<T> other) {
        return src -> src
                .map(Maybe::just)
                .defaultIfEmpty(other)
                .flatMap(m -> m);
    }

    public static <T> MaybeTransformer<T, T> orElse(Callable<Maybe<T>> other) {
        return orElse(Maybe.defer(other));
    }

    public static <T> MaybeTransformer<T, T> startNow() {
        return src -> {
            src = src.cache();
            Disposable subscription = src.subscribe();
            return src.doAfterTerminate(subscription::dispose);
        };
    }
}
