package com.slimgears.util.rx;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.slimgears.util.rx.Maybes.retryPolicy;

public class Completables {
    public static CompletableTransformer startNow() {
        return src -> {
            src = src.cache();
            Disposable subscription = src.subscribe();
            return src.doAfterTerminate(subscription::dispose);
        };
    }

    public static CompletableTransformer doWhileWaiting(Duration interval, Consumer<Duration> action) {
        return src -> {
            Observable<Duration> timer = Observables.timerObservable(interval);
            AtomicReference<Disposable> subscription = new AtomicReference<>(Disposables.empty());
            return src
                    .doOnSubscribe(d -> subscription.getAndSet(timer.subscribe(action)).dispose())
                    .doFinally(() -> subscription.getAndSet(Disposables.empty()).dispose());
        };
    }

    public static CompletableTransformer doWhileWaiting(Duration interval, Action action) {
        return doWhileWaiting(interval, duration -> action.run());
    }

    public static CompletableTransformer backOffDelayRetry(Predicate<Throwable> errorPredicate, Duration initialDelay, int maxErrors) {
        return upstream -> Completable.defer(() -> {
                AtomicInteger attemptsMade = new AtomicInteger(0);
                return upstream
                        .doOnComplete(() -> attemptsMade.lazySet(0))
                        .retryWhen(retryPolicy(errorPredicate, attemptsMade, initialDelay, maxErrors));
            });
    }
}
