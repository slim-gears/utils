package com.slimgears.util.rx;

import io.reactivex.Flowable;
import io.reactivex.SingleTransformer;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Singles {
    private final static Logger log = Logger.getLogger(Singles.class.getName());

    public static <T> SingleTransformer<T, T> backoffDelayRetry(Duration initialDelay, int maxErrors) {
        return upstream -> {
            AtomicInteger attemptsMade = new AtomicInteger(0);
            return upstream
                    .doOnSuccess(v -> attemptsMade.lazySet(0))
                    .retryWhen(err -> err.flatMap(e -> {
                        long delayMillis = (1 << attemptsMade.get()) * initialDelay.toMillis();
                        return (attemptsMade.incrementAndGet() > maxErrors)
                                ? Flowable.<T>error(e)
                                : Flowable.timer(delayMillis, TimeUnit.MILLISECONDS)
                                .doOnNext(v -> log.warning("Error occurred: " + e.getMessage() + " Retry #" + attemptsMade + " (delay: " + delayMillis + " milliseconds)" + e));
                    }));
        };
    }
}
