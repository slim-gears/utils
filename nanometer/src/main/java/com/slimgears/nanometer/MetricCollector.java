package com.slimgears.nanometer;

import com.slimgears.nanometer.internal.*;
import io.reactivex.CompletableTransformer;
import io.reactivex.MaybeTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface MetricCollector extends Taggable<MetricCollector>, MetricFilter {
    interface Counter {
        default void increment() {
            increment(1.0);
        }

        void increment(double value);
    }

    interface Gauge {
        <T, N extends Number> void record(T object, Function<T, N> valueProducer);

        <N extends Number> void record(N number);

        default <N extends Number> void record(Supplier<N> valueProducer) {
            record(valueProducer, Supplier::get);
        }

        default void record(AtomicInteger value) {
            record((Supplier<Double>) value::doubleValue);
        }

        default void record(AtomicLong value) {
            record((Supplier<Double>) value::doubleValue);
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

        default Runnable wrap(Runnable runnable) {
            return () -> record(runnable);
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
        Async countActiveSubscriptions(String name, MetricTag... tags);
        Async countErrors(String name, MetricTag... tags);
        Async countCompletions(String name, MetricTag... tags);
        Async timeTillFirst(String name, MetricTag... tags);
        Async timeTillComplete(String name, MetricTag... tags);
        Async timeBetweenItems(String name, MetricTag... tags);

        <T> ObservableTransformer<T, T> forObservable();
        <T> MaybeTransformer<T, T> forMaybe();
        <T> SingleTransformer<T, T> forSingle();
        CompletableTransformer forCompletable();
        <T> Stream<T> forStream(Stream<T> stream);
    }

    Counter counter(String name, MetricTag... tags);
    Timer timer(String name, MetricTag... tags);
    Gauge gauge(String name, MetricTag... tags);

    interface Factory {
        MetricCollector create();
        Factory filter(MetricFilter filter);

        default Factory name(String name) {
            Factory self = this;
            return new Factory() {
                @Override
                public MetricCollector create() {
                    return self.create().name(name);
                }

                @Override
                public Factory filter(MetricFilter filter) {
                    return self.filter(filter);
                }
            };
        }

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

    default Async asyncDefault() {
        return DefaultAsyncMetricCollector.create(this)
                .countSubscriptions("totalSubscriptionCount")
                .countActiveSubscriptions("activeSubscriptionCount")
                .countCompletions("completeCount")
                .countErrors("errorCount")
                .countItems("itemCount")
                .timeTillFirst("timeTillFirst")
                .timeTillComplete("timeTillComplete")
                .timeBetweenItems("timeBetweenItems");
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

    static MetricCollector empty() {
        return EmptyMetricCollector.instance;
    }
}
