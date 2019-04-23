package com.slimgears.util.rx;

import io.reactivex.SingleTransformer;
import io.reactivex.disposables.Disposable;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import static com.slimgears.util.rx.Maybes.retryPolicy;

public class Singles {
    private final static Logger log = Logger.getLogger(Singles.class.getName());

    public static <T> SingleTransformer<T, T> backoffDelayRetry(Duration initialDelay, int maxErrors) {
        return upstream -> {
            AtomicInteger attemptsMade = new AtomicInteger(0);
            return upstream
                    .doOnSuccess(v -> attemptsMade.lazySet(0))
                    .retryWhen(retryPolicy(attemptsMade, initialDelay, maxErrors));
        };
    }

    public static <T> SingleTransformer<T, T> startNow() {
        return src -> {
            src = src.cache();
            Disposable subscription = src.subscribe();
            return src.doAfterTerminate(subscription::dispose);
        };
    }
}
