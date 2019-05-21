package com.slimgears.util.rx;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import org.reactivestreams.Publisher;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("WeakerAccess")
public class Observables {
    public static <T> ObservableTransformer<T, T> startNow() {
        return src -> {
            src = src.cache();
            Disposable subscription = src.subscribe();
            return src.doAfterTerminate(subscription::dispose);
        };
    }

    public static <T> ObservableTransformer<T, T> backOffDelayRetry(Duration initialDelay, int maxErrors) {
        return backOffDelayRetry(t -> true, initialDelay, maxErrors);
    }

    public static <T> ObservableTransformer<T, T> backOffDelayRetry(Predicate<Throwable> predicate, Duration initialDelay, int maxErrors) {
        return upstream -> {
            AtomicInteger attemptsMade = new AtomicInteger(0);
            return upstream
                    .doOnNext(v -> attemptsMade.lazySet(0))
                    .retryWhen(retryPolicy(predicate, attemptsMade, initialDelay, maxErrors));
        };
    }

    private static Function<Observable<Throwable>, Observable<?>> retryPolicy(Predicate<Throwable> errorPredicate, AtomicInteger attemptsMade, Duration initialDelay, int maxErrors) {
        Function<Flowable<Throwable>, Publisher<?>> retryPolicy = Maybes.retryPolicy(errorPredicate, attemptsMade, initialDelay, maxErrors);
        return err -> Observable.fromPublisher(retryPolicy.apply(err.toFlowable(BackpressureStrategy.LATEST)));
    }

    public static <T> ObservableTransformer<T, List<T>> bufferUntilIdle(Duration maxIdleDuration) {
        return bufferUntilIdleOrTimeout(maxIdleDuration, Duration.ofMillis(maxIdleDuration.toMillis() * 4));
    }

    @SuppressWarnings("WeakerAccess")
    public static <T> ObservableTransformer<T, List<T>> bufferUntilIdleOrTimeout(Duration maxIdleDuration, Duration maxBufferDuration) {
        return source -> new BufferUntilIdleObservableSource<>(source, maxIdleDuration, maxBufferDuration);
    }
}
