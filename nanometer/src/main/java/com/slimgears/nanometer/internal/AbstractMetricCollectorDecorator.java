package com.slimgears.nanometer.internal;

import com.slimgears.nanometer.MetricCollector;
import com.slimgears.nanometer.MetricLevel;
import com.slimgears.nanometer.MetricTag;

public class AbstractMetricCollectorDecorator implements MetricCollector {
    private final MetricCollector underlyingCollector;

    protected AbstractMetricCollectorDecorator(MetricCollector underlyingCollector) {
        this.underlyingCollector = underlyingCollector;
    }

    protected MetricCollector getUnderlyingCollector() {
        return underlyingCollector;
    }

    @Override
    public Counter counter(String name, MetricTag... tags) {
        return getUnderlyingCollector().counter(name, tags);
    }

    @Override
    public Timer timer(String name, MetricTag... tags) {
        return getUnderlyingCollector().timer(name, tags);
    }

    @Override
    public Gauge gauge(String name, MetricTag... tags) {
        return getUnderlyingCollector().gauge(name, tags);
    }

    @Override
    public MetricLevel getLevel(String name, MetricTag... tags) {
        return getUnderlyingCollector().getLevel(name, tags);
    }
}
