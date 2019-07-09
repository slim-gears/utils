package com.slimgears.util.rx;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeTransformer;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import org.reactivestreams.Publisher;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("WeakerAccess")
public class Maybes {
    public static <T> MaybeTransformer<T, T> startNow() {
        return src -> {
            src = src.cache();
            Disposable subscription = src.subscribe();
            return src.doAfterTerminate(subscription::dispose);
        };
    }

    public static <T> MaybeTransformer<T, T> backOffDelayRetry(Duration initialDelay, int maxErrors) {
        return backOffDelayRetry(t -> true, initialDelay, maxErrors);
    }

    public static <T> MaybeTransformer<T, T> backOffDelayRetry(Predicate<Throwable> predicate, Duration initialDelay, int maxErrors) {
        return upstream -> {
            AtomicInteger attemptsMade = new AtomicInteger(0);
            return upstream
                    .doOnSuccess(v -> attemptsMade.lazySet(0))
                    .retryWhen(retryPolicy(predicate, attemptsMade, initialDelay, maxErrors));
        };
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T> Maybe<T> fromOptional(Optional<T> optional) {
        return optional.map(Maybe::just).orElseGet(Maybe::empty);
    }

    public static <T> Maybe<T> fromOptional(@Nullable T optional) {
        return Maybe.fromCallable(() -> optional);
    }

    static Function<Flowable<Throwable>, Publisher<?>> retryPolicy(Predicate<Throwable> predicate, AtomicInteger attemptsMade, Duration initialDelay, int maxErrors) {
        return err -> err.flatMap(e -> {
            long delayMillis = (1 << attemptsMade.get()) * initialDelay.toMillis();
            return !predicate.test(e) || (attemptsMade.incrementAndGet() > maxErrors)
                    ? Flowable.error(e)
                    : Flowable.timer(delayMillis, TimeUnit.MILLISECONDS);
        });
    }
}
