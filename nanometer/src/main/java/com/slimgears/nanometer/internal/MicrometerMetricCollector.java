package com.slimgears.nanometer.internal;

import com.slimgears.nanometer.MetricFilter;
import com.slimgears.nanometer.MetricTag;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
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
        private final io.micrometer.core.instrument.MeterRegistry registry;
        private final String name;
        private final Iterable<Tag> tags;

        MicrometerGauge(MeterRegistry registry, String name, Iterable<Tag> tags) {
            this.registry = registry;
            this.name = name;
            this.tags = tags;
        }

        @Override
        public <T> void record(T object, ToDoubleFunction<T> valueProducer) {
            registry.gauge(name, tags, object, valueProducer);
        }

        @Override
        public <N extends Number> void record(N number) {
            registry.gauge(name, tags, number);
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
