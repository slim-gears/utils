package com.slimgears.nanometer.internal;

import com.slimgears.nanometer.MetricCollector;
import com.slimgears.nanometer.MetricTag;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CachedMetricCollectorDecorator extends AbstractMetricCollectorDecorator {
    private final Map<String, Gauge> gaugeCache = new ConcurrentHashMap<>();
    private final Map<String, Timer> timerCache = new ConcurrentHashMap<>();
    private final Map<String, Counter> counterCache = new ConcurrentHashMap<>();

    private CachedMetricCollectorDecorator(MetricCollector underlyingCollector) {
        super(underlyingCollector);
    }

    public static MetricCollector decorate(MetricCollector collector) {
        return new CachedMetricCollectorDecorator(collector);
    }

    @Override
    public Counter counter(String name, MetricTag... tags) {
        return counterCache.computeIfAbsent(toKey(name, tags), k -> super.counter(name, tags));
    }

    @Override
    public Timer timer(String name, MetricTag... tags) {
        return timerCache.computeIfAbsent(toKey(name, tags), k -> super.timer(name, tags));
    }

    @Override
    public Gauge gauge(String name, MetricTag... tags) {
        return gaugeCache.computeIfAbsent(toKey(name, tags), k -> super.gauge(name, tags));
    }

    private String toKey(String name, MetricTag[] tag) {
        return name + Arrays
                .stream(tag).map(t -> t.key() + "=" + t.value())
                .collect(Collectors.joining(";", "[", "]"));

    }
}
