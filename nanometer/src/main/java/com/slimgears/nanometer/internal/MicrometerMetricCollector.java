package com.slimgears.nanometer.internal;

import com.slimgears.nanometer.MetricFilter;
import com.slimgears.nanometer.MetricTag;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

class MicrometerMetricCollector extends AbstractMetricCollector {
    private final MeterRegistry registry;

    MicrometerMetricCollector(MeterRegistry registry, MetricFilter filter) {
        super(filter);
        this.registry = registry;
    }

    static class MicrometerCounter implements Counter {
        private final io.micrometer.core.instrument.Counter counter;

        MicrometerCounter(io.micrometer.core.instrument.Counter counter) {
            this.counter = counter;
        }

        @Override
        public void increment(double value) {
            counter.increment(value);
        }
    }

    static class MicrometerTimer implements Timer {
        private final io.micrometer.core.instrument.Timer timer;

        MicrometerTimer(io.micrometer.core.instrument.Timer timer) {
            this.timer = timer;
        }

        @Override
        public void record(Duration duration) {
            timer.record(duration);
        }
    }

    static class MicrometerGauge implements Gauge {
        private final AtomicReference<Double> value = new AtomicReference<>(0.0);
        private final AtomicReference<Supplier<Double>> valueSupplier = new AtomicReference<>(value::get);

        MicrometerGauge(MeterRegistry registry, String name, Iterable<Tag> tags) {
            registry.gauge(name, tags, valueSupplier, vs -> vs.get().get());
        }

        @Override
        public <T, N extends Number> void record(T object, Function<T, N> valueProducer) {
            valueSupplier.set(() -> valueProducer.apply(object).doubleValue());
        }

        @Override
        public <N extends Number> void record(N number) {
            value.set(number.doubleValue());
        }
    }

    @Override
    public Counter counter(String name, MetricTag... tags) {
        return new MicrometerCounter(registry.counter(name, toMicrometerTags(tags)));
    }

    @Override
    public Timer timer(String name, MetricTag... tags) {
        return new MicrometerTimer(registry.timer(name, toMicrometerTags(tags)));
    }

    @Override
    public Gauge gauge(String name, MetricTag... tags) {
        return new MicrometerGauge(registry, name, toMicrometerTags(tags));
    }

    private Iterable<Tag> toMicrometerTags(MetricTag[] tags) {
        return tags.length > 0
                ? Arrays.stream(tags)
                .map(t -> Tag.of(t.key(), t.value()))
                .collect(Collectors.toList())
                : Collections.emptyList();
    }
}
