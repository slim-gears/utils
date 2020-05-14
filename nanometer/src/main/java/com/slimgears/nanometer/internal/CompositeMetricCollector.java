package com.slimgears.nanometer.internal;

import com.slimgears.nanometer.MetricCollector;
import com.slimgears.nanometer.MetricLevel;
import com.slimgears.nanometer.MetricTag;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

public class CompositeMetricCollector extends AbstractMetricCollector {
    private final Collection<MetricCollector> metricCollectors;

    private CompositeMetricCollector(Collection<MetricCollector> metricCollectors) {
        this.metricCollectors = metricCollectors;
    }

    public static MetricCollector of(Collection<MetricCollector> metricCollectors) {
        if (metricCollectors.isEmpty()) {
            return EmptyMetricCollector.instance;
        } else if (metricCollectors.size() == 1) {
            return metricCollectors.iterator().next();
        } else {
            return new CompositeMetricCollector(metricCollectors);
        }
    }

    public static MetricCollector of(MetricCollector... metricCollectors) {
        return of(Arrays.asList(metricCollectors));
    }

    @Override
    public MetricCollector.Counter counter(String name, MetricTag... tags) {
        return new CompositeCounter(metricCollectors.stream().map(mc -> mc.counter(name, tags))
                .collect(Collectors.toList()));
    }

    @Override
    public MetricCollector.Timer timer(String name, MetricTag... tags) {
        return new CompositeTimer(metricCollectors.stream().map(mc -> mc.timer(name, tags))
                .collect(Collectors.toList()));
    }

    @Override
    public MetricCollector.Gauge gauge(String name, MetricTag... tags) {
        return new CompositeGauge(metricCollectors.stream().map(mc -> mc.gauge(name, tags))
                .collect(Collectors.toList()));
    }

    @Override
    public MetricLevel getLevel(String name, MetricTag... tags) {
        return metricCollectors.stream()
                .map(mc -> mc.getLevel(name, tags))
                .max(Comparator.comparing(MetricLevel::ordinal))
                .orElse(MetricLevel.UNFILTERED);
    }

    static class CompositeGauge implements MetricCollector.Gauge {
        private final Collection<MetricCollector.Gauge> gauges;

        CompositeGauge(Collection<MetricCollector.Gauge> gauges) {
            this.gauges = gauges;
        }

        @Override
        public <T, N extends Number> void record(T object, Function<T, N> valueProducer) {
            gauges.forEach(g -> g.record(object, valueProducer));
        }

        @Override
        public <N extends Number> void record(N number) {
            gauges.forEach(g -> g.record(number));
        }
    }

    static class CompositeTimer implements MetricCollector.Timer {
        private final Collection<MetricCollector.Timer> timers;

        CompositeTimer(Collection<MetricCollector.Timer> timers) {
            this.timers = timers;
        }

        @Override
        public void record(Duration duration) {
            timers.forEach(t -> t.record(duration));
        }
    }

    static class CompositeCounter implements MetricCollector.Counter {
        private final Collection<MetricCollector.Counter> counters;

        CompositeCounter(Collection<MetricCollector.Counter> counters) {
            this.counters = counters;
        }

        @Override
        public void increment(double value) {
            counters.forEach(c -> c.increment(value));
        }
    }
}
