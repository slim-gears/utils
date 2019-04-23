package com.slimgears.util.rx;

import io.reactivex.Flowable;
import io.reactivex.MaybeTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import org.reactivestreams.Publisher;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Maybes {
    private final static Logger log = Logger.getLogger(Singles.class.getName());

    public static <T> MaybeTransformer<T, T> startNow() {
        return src -> {
            src = src.cache();
            Disposable subscription = src.subscribe();
            return src.doAfterTerminate(subscription::dispose);
        };
    }

    public static <T> MaybeTransformer<T, T> backoffDelayRetry(Duration initialDelay, int maxErrors) {
        return upstream -> {
            AtomicInteger attemptsMade = new AtomicInteger(0);
            return upstream
                    .doOnSuccess(v -> attemptsMade.lazySet(0))
                    .retryWhen(retryPolicy(attemptsMade, initialDelay, maxErrors));
        };
    }

    static Function<Flowable<Throwable>, Publisher<?>> retryPolicy(AtomicInteger attemptsMade, Duration initialDelay, int maxErrors) {
        return err -> err.flatMap(e -> {
            long delayMillis = (1 << attemptsMade.get()) * initialDelay.toMillis();
            return (attemptsMade.incrementAndGet() > maxErrors)
                    ? Flowable.error(e)
                    : Flowable.timer(delayMillis, TimeUnit.MILLISECONDS)
                    .doOnNext(v -> log.warning(() -> "Error occurred: " + e.getMessage() + " Retry #" + attemptsMade + " (delay: " + delayMillis + " milliseconds)" + e));
        });
    }
}
