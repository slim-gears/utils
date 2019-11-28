package com.slimgears.nanometer;

import com.slimgears.nanometer.internal.*;
import io.reactivex.*;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;

public interface MetricCollector extends Taggable<MetricCollector>, MetricFilter {
    interface Counter {
        default void increment() {
            increment(1.0);
        }

        void increment(double value);
    }

    interface Gauge {
        <T> void record(T object, ToDoubleFunction<T> valueProducer);

        default <N extends Number> void record(N number) {}

        default void record(Supplier<Double> valueProducer) {
            record(valueProducer, Supplier::get);
        }
    }

    interface Timer {
        void record(Duration duration);

        interface Stopper {
            Stopper start();
            Stopper stop();
        }

        default Stopper stopper() {
            return new Stopper() {
                private final AtomicReference<Date> begin = new AtomicReference<>();

                @Override
                public Stopper start() {
                    begin.set(new Date());
                    return this;
                }

                @Override
                public Stopper stop() {
                    Date end = new Date();
                    Duration duration = Optional
                            .ofNullable(begin.get())
                            .map(b -> Duration.ofMillis(end.getTime() - b.getTime()))
                            .orElse(Duration.ofMillis(0));
                    record(duration);
                    return this;
                }
            };
        }

        default void record(Runnable runnable) {
            Stopper stopper = stopper().start();
            try {
                runnable.run();
            } finally {
                stopper.stop();
            }
        }

        default <T> T record(Supplier<T> supplier) {
            Stopper stopper = stopper().start();
            try {
                return supplier.get();
            } finally {
                stopper.stop();
            }
        }
    }

    interface Async {
        Async countItems(String name, MetricTag... tags);
        Async countSubscriptions(String name, MetricTag... tags);
        Async countErrors(String name, MetricTag... tags);
        Async countCompletions(String name, MetricTag... tags);
        Async timeTillFirst(String name, MetricTag... tags);
        Async timeTillComplete(String name, MetricTag... tags);
        Async timeBetweenItems(String name, MetricTag... tags);

        <T> ObservableOperator<T, T> forObservable();
        <T> MaybeOperator<T, T> forMaybe();
        <T> SingleOperator<T, T> forSingle();
        CompletableOperator forCompletable();
        <T> Stream<T> forStream(Stream<T> stream);
    }

    Counter counter(String name, MetricTag... tags);
    Timer timer(String name, MetricTag... tags);
    Gauge gauge(String name, MetricTag... tags);

    interface Factory {
        MetricCollector create();
        Factory filter(MetricFilter filter);

        default Factory level(MetricLevel level) {
            return filter((name, tags) -> level);
        }

        default MetricCollector createForClass(Class<?> clazz) {
            return MetricAnnotations.decorate(create(), clazz);
        }

        default Factory bind(Binder... binders) {
            Arrays.asList(binders).forEach(b -> b.bindTo(this));
            return this;
        }

        default Factory bind(Iterable<Binder> binders) {
            binders.forEach(b -> b.bindTo(this));
            return this;
        }

        default Factory autoBind() {
            ServiceLoader.load(Binder.class, ClassLoader.getSystemClassLoader())
                    .iterator()
                    .forEachRemaining(this::bind);
            return this;
        }

        static Factory composite(Factory... factories) {
            return composite(Arrays.asList(factories));
        }

        static Factory composite(Collection<Factory> factories) {
            return CompositeMetricCollectorFactory.create(factories);
        }

        static Factory autoLoading() {
            return AutoLoadingCollectorFactory.newInstance();
        }

        static Factory autoLoading(Supplier<MetricCollector.Factory> fallback) {
            return AutoLoadingCollectorFactory.newInstance(fallback);
        }
    }

    interface Binder {
        void bindTo(MetricCollector.Factory factory);
    }

    default Async async() {
        return DefaultAsyncMetricCollector.create(this);
    }

    default MetricCollector name(String name) {
        return WithNameAndTagsMetricCollectorDecorator.decorate(this, name);
    }

    default MetricCollector level(MetricLevel level) {
        return WithLevelMetricCollectorDecorator.decorate(this, level);
    }

    default MetricCollector info() {
        return level(MetricLevel.INFO);
    }

    default MetricCollector warning() {
        return level(MetricLevel.WARNING);
    }

    default MetricCollector error() {
        return level(MetricLevel.ERROR);
    }

    default MetricCollector debug() {
        return level(MetricLevel.DEBUG);
    }

    default MetricCollector trace() {
        return level(MetricLevel.TRACE);
    }

    default MetricCollector tags(MetricTag... tags) {
        return WithNameAndTagsMetricCollectorDecorator.decorate(this, "", tags);
    }

    static MetricCollector composite(MetricCollector... collectors) {
        return CompositeMetricCollector.of(collectors);
    }
}
