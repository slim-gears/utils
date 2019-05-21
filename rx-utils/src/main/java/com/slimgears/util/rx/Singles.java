package com.slimgears.util.rx;

import io.reactivex.SingleTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static com.slimgears.util.rx.Maybes.retryPolicy;

@SuppressWarnings("WeakerAccess")
public class Singles {
    public static <T> SingleTransformer<T, T> backOffDelayRetry(Duration initialDelay, int maxErrors) {
        return backOffDelayRetry(t -> true, initialDelay, maxErrors);
    }

    public static <T> SingleTransformer<T, T> backOffDelayRetry(Predicate<Throwable> errorPredicate, Duration initialDelay, int maxErrors) {
        return upstream -> {
            AtomicInteger attemptsMade = new AtomicInteger(0);
            return upstream
                    .doOnSuccess(v -> attemptsMade.lazySet(0))
                    .retryWhen(retryPolicy(errorPredicate, attemptsMade, initialDelay, maxErrors));
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
