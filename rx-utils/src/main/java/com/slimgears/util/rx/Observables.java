package com.slimgears.util.rx;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
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
                    .retryWhen(err -> err.flatMap(e -> {
                        long delayMillis = (1 << attemptsMade.get()) * initialDelay.toMillis();
                        return (attemptsMade.incrementAndGet() > maxErrors)
                                ? Observable.<T>error(e)
                                : Observable.timer(delayMillis, TimeUnit.MILLISECONDS)
                                .doOnNext(v -> log.warning("Error occurred: " + e.getMessage() + " Retry #" + attemptsMade + " (delay: " + delayMillis + " milliseconds)" + e));
                    }));
        };
    }

    public static <T> ObservableTransformer<T, List<T>> bufferUntilIdle(Duration maxIdleDuration) {
        return bufferUntilIdleOrFull(maxIdleDuration, Integer.MAX_VALUE);
    }

    public static <T> ObservableTransformer<T, List<T>> bufferUntilIdleOrFull(Duration maxIdleDuration, int maxSize) {
        AtomicReference<List<T>> currentBuffer = new AtomicReference<>(new ArrayList<>());
        AtomicReference<Disposable> currentTimerDisposable = new AtomicReference<>(Disposables.empty());
        return source -> Observable.create(emitter -> {
            Action flush = () -> {
                Optional
                        .of(currentBuffer.getAndSet(new ArrayList<>()))
                        .filter(b -> !b.isEmpty())
                        .ifPresent(emitter::onNext);
                currentTimerDisposable.getAndSet(Disposables.empty()).dispose();
            };

            emitter.setDisposable(source.subscribe(
                    next -> {
                        List<T> buf = currentBuffer.get();
                        buf.add(next);
                        if (buf.size() >= maxSize) {
                            flush.run();
                        } else {
                            currentTimerDisposable
                                    .getAndSet(Completable
                                            .timer(maxIdleDuration.toMillis(), TimeUnit.MILLISECONDS)
                                            .subscribe(flush))
                                    .dispose();
                        }
                    },
                    emitter::onError,
                    () -> {
                        flush.run();
                        emitter.onComplete();
                    }));
        });
    }
}
