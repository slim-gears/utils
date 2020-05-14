package com.slimgears.nanometer.internal;

import com.slimgears.nanometer.MetricCollector;
import com.slimgears.nanometer.MetricTag;
import io.reactivex.*;
import io.reactivex.disposables.Disposable;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DefaultAsyncMetricCollector implements MetricCollector.Async {
    private final static Runnable emptyRunnable = () -> {};
    private final MetricCollector metricCollector;
    private final AtomicReference<Supplier<Runnable>> doOnNext = new AtomicReference<>();
    private final AtomicReference<Supplier<Runnable>> doOnSubscribe = new AtomicReference<>();
    private final AtomicReference<Supplier<Runnable>> doOnComplete = new AtomicReference<>();
    private final AtomicReference<Supplier<Runnable>> doOnError = new AtomicReference<>();
    private final AtomicReference<Supplier<Runnable>> doFinally = new AtomicReference<>();

    private DefaultAsyncMetricCollector(MetricCollector metricCollector) {
        this.metricCollector = metricCollector;
    }

    public static MetricCollector.Async create(MetricCollector collector) {
        return new DefaultAsyncMetricCollector(collector);
    }

    @Override
    public MetricCollector.Async countItems(String name, MetricTag... tags) {
        MetricCollector.Counter counter = metricCollector.counter(name, tags);
        return addRunnable(doOnNext, counter::increment);
    }

    @Override
    public MetricCollector.Async countSubscriptions(String name, MetricTag... tags) {
        MetricCollector.Counter counter = metricCollector.counter(name, tags);
        return addRunnable(doOnSubscribe, counter::increment);
    }

    @Override
    public MetricCollector.Async countActiveSubscriptions(String name, MetricTag... tags) {
        AtomicInteger currentSubscriptions = new AtomicInteger();
        metricCollector.gauge(name, tags).record(currentSubscriptions, AtomicInteger::doubleValue);
        addRunnable(doOnSubscribe, currentSubscriptions::incrementAndGet);
        return addRunnable(doFinally, currentSubscriptions::decrementAndGet);
    }

    @Override
    public MetricCollector.Async countErrors(String name, MetricTag... tags) {
        MetricCollector.Counter counter = metricCollector.counter(name, tags);
        return addRunnable(doOnError, counter::increment);
    }

    @Override
    public MetricCollector.Async countCompletions(String name, MetricTag... tags) {
        MetricCollector.Counter counter = metricCollector.counter(name, tags);
        return addRunnable(doOnComplete, counter::increment);
    }

    @Override
    public MetricCollector.Async timeTillFirst(String name, MetricTag... tags) {
        return addDeferred(doOnNext, () -> {
            AtomicBoolean isStopped = new AtomicBoolean(false);
            MetricCollector.Timer.Stopper stopper = metricCollector.timer(name, tags)
                    .stopper()
                    .start();
            return () -> {
                if (isStopped.compareAndSet(false, true)) {
                    stopper.stop();
                }
            };
        });
    }

    @Override
    public MetricCollector.Async timeTillComplete(String name, MetricTag... tags) {
        return addDeferred(doOnComplete, () -> {
            MetricCollector.Timer.Stopper stopper = metricCollector.timer(name, tags)
                    .stopper()
                    .start();
            return stopper::stop;
        });
    }

    @Override
    public MetricCollector.Async timeBetweenItems(String name, MetricTag... tags) {
        return addDeferred(doOnNext, () -> {
            AtomicReference<MetricCollector.Timer.Stopper> stopper =
                    new AtomicReference<>(metricCollector.timer(name, tags).stopper().start());
            return () -> stopper.getAndSet(metricCollector.timer(name, tags).stopper().start()).stop();
        });
    }

    @Override
    public <T> ObservableTransformer<T, T> forObservable() {
        return source -> {
            Runnable onNext = toRunnable(doOnNext);
            Runnable onComplete = toRunnable(doOnComplete);
            Runnable onSubscribe = toRunnable(doOnSubscribe);
            Runnable onError = toRunnable(doOnError);
            Runnable onFinally = toRunnable(doFinally);

            ObservableOperator<T, T> operator = observer -> new Observer<T>() {
                @Override
                public void onSubscribe(Disposable d) {
                    onSubscribe.run();
                    observer.onSubscribe(d);
                }

                @Override
                public void onNext(T t) {
                    onNext.run();
                    observer.onNext(t);
                }

                @Override
                public void onError(Throwable e) {
                    onError.run();
                    observer.onError(e);
                }

                @Override
                public void onComplete() {
                    onComplete.run();
                    observer.onComplete();
                }
            };
            return source
                    .lift(operator)
                    .doFinally(onFinally::run);
        };
    }

    @Override
    public <T> MaybeTransformer<T, T> forMaybe() {
        return source -> {
            Runnable onNext = toRunnable(doOnNext);
            Runnable onComplete = toRunnable(doOnComplete);
            Runnable onSubscribe = toRunnable(doOnSubscribe);
            Runnable onError = toRunnable(doOnError);
            Runnable onFinally = toRunnable(doFinally);

            MaybeOperator<T, T> operator = observer -> new MaybeObserver<T>() {
                @Override
                public void onSubscribe(Disposable d) {
                    onSubscribe.run();
                    observer.onSubscribe(d);
                }

                @Override
                public void onSuccess(T t) {
                    onNext.run();
                    onComplete.run();
                    observer.onSuccess(t);
                }

                @Override
                public void onError(Throwable e) {
                    onError.run();
                    observer.onError(e);
                }

                @Override
                public void onComplete() {
                    onComplete.run();
                    observer.onComplete();
                }
            };

            return source
                    .lift(operator)
                    .doFinally(onFinally::run);
        };
    }

    @Override
    public <T> SingleTransformer<T, T> forSingle() {
        return source -> {
            Runnable onNext = toRunnable(doOnNext);
            Runnable onComplete = toRunnable(doOnComplete);
            Runnable onSubscribe = toRunnable(doOnSubscribe);
            Runnable onError = toRunnable(doOnError);
            Runnable onFinally = toRunnable(doFinally);

            SingleOperator<T, T> operator = observer -> new SingleObserver<T>() {
                @Override
                public void onSubscribe(Disposable d) {
                    onSubscribe.run();
                    observer.onSubscribe(d);
                }

                @Override
                public void onSuccess(T t) {
                    onNext.run();
                    onComplete.run();
                    observer.onSuccess(t);
                }

                @Override
                public void onError(Throwable e) {
                    onError.run();
                    observer.onError(e);
                }
            };

            return source
                    .lift(operator)
                    .doFinally(onFinally::run);
        };
    }

    @Override
    public CompletableTransformer forCompletable() {
        return source -> {
            Runnable onComplete = toRunnable(doOnComplete);
            Runnable onSubscribe = toRunnable(doOnSubscribe);
            Runnable onError = toRunnable(doOnError);
            Runnable onFinally = toRunnable(doFinally);

            CompletableOperator operator = observer -> new CompletableObserver() {
                @Override
                public void onSubscribe(Disposable d) {
                    onSubscribe.run();
                    observer.onSubscribe(d);
                }

                @Override
                public void onComplete() {
                    onComplete.run();
                    observer.onComplete();
                }

                @Override
                public void onError(Throwable e) {
                    onError.run();
                    observer.onError(e);
                }
            };

            return source.lift(operator)
                    .doFinally(onFinally::run);
        };
    }

    @Override
    public <T> Stream<T> forStream(Stream<T> stream) {
        Runnable onNext = toRunnable(doOnNext);
        Runnable onComplete = toRunnable(doOnComplete);
        Runnable onSubscribe = toRunnable(doOnSubscribe);
        Runnable onFinally = toRunnable(doFinally);

        onSubscribe.run();
        return stream
                .onClose(combine(onComplete, onFinally))
                .peek(val -> onNext.run());
    }

    private MetricCollector.Async addDeferred(AtomicReference<Supplier<Runnable>> runnable, Supplier<Runnable> newRunnable) {
        runnable.updateAndGet(existingRunnable -> Optional
                .ofNullable(existingRunnable)
                .map(a -> combine(a, newRunnable))
                .orElse(newRunnable));
        return this;
    }

    private MetricCollector.Async addRunnable(AtomicReference<Supplier<Runnable>> runnable, Runnable newRunnable) {
        return addDeferred(runnable, () -> newRunnable);
    }

    private Supplier<Runnable> combine(Supplier<Runnable> a1, Supplier<Runnable> a2) {
        return () -> combine(a1.get(), a2.get());
    }

    private Runnable combine(Runnable a1, Runnable a2) {
        return () -> {
            a1.run();
            a2.run();
        };
    }

    private Runnable toRunnable(AtomicReference<Supplier<Runnable>> runnableSupplier) {
        return Optional.ofNullable(runnableSupplier.get()).map(Supplier::get).orElse(emptyRunnable);
    }
}
