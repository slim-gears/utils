package com.slimgears.util.rx;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Observables {
    private final static Logger log = Logger.getLogger(Observables.class.getName());

    public static <T> ObservableTransformer<T, T> startNow() {
        return src -> {
            src = src.cache();
            Disposable subscription = src.subscribe();
            return src.doAfterTerminate(subscription::dispose);
        };
    }

    public static <T> ObservableTransformer<T, T> backoffDelayRetry(Duration initialDelay, int maxErrors) {
        return upstream -> {
            AtomicInteger attemptsMade = new AtomicInteger(0);
            return upstream
                    .doOnNext(v -> attemptsMade.lazySet(0))
                    .retryWhen(retryPolicy(attemptsMade, initialDelay, maxErrors));
        };
    }

    private static Function<Observable<Throwable>, Observable<?>> retryPolicy(AtomicInteger attemptsMade, Duration initialDelay, int maxErrors) {
        return err -> err.flatMap(e -> {
            long delayMillis = (1 << attemptsMade.get()) * initialDelay.toMillis();
            return (attemptsMade.incrementAndGet() > maxErrors)
                    ? Observable.error(e)
                    : Observable.timer(delayMillis, TimeUnit.MILLISECONDS)
                    .doOnNext(v -> log.warning(() -> "Error occurred: " + e.getMessage() + " Retry #" + attemptsMade + " (delay: " + delayMillis + " milliseconds)" + e));
        });
    }

    public static <T> ObservableTransformer<T, List<T>> bufferUntilIdle(Duration maxIdleDuration) {
        return bufferUntilIdleOrTimeout(maxIdleDuration, Duration.ofMillis(maxIdleDuration.toMillis() * 4));
    }

    @SuppressWarnings("WeakerAccess")
    public static <T> ObservableTransformer<T, List<T>> bufferUntilIdleOrTimeout(Duration maxIdleDuration, Duration maxBufferDuration) {
        return source -> new BufferUntilIdleObservableSource<>(source, maxIdleDuration, maxBufferDuration);
    }
}
