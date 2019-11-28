package com.slimgears.nanometer.internal;

import com.slimgears.nanometer.MetricCollector;
import com.slimgears.nanometer.MetricLevel;
import com.slimgears.nanometer.MetricTag;

public class WithLevelMetricCollectorDecorator implements MetricCollector {
    private final static MetricCollector emptyCollector = EmptyMetricCollector.instance;
    private final MetricCollector underlying;
    private final MetricLevel reportLevel;

    private WithLevelMetricCollectorDecorator(MetricCollector underlying, MetricLevel level) {
        this.underlying = underlying;
        this.reportLevel = level;
    }

    public static MetricCollector decorate(MetricCollector underlying, MetricLevel level) {
        return level != MetricLevel.UNFILTERED
                ? new WithLevelMetricCollectorDecorator(underlying, level)
                : underlying;
    }

    @Override
    public Counter counter(String name, MetricTag... tags) {
        return shouldReport(name, tags)
                ? underlying.counter(name, tags)
                : emptyCollector.counter(name, tags);
    }

    @Override
    public Timer timer(String name, MetricTag... tags) {
        return shouldReport(name, tags)
                ? underlying.timer(name, tags)
                : emptyCollector.timer(name, tags);
    }

    @Override
    public Gauge gauge(String name, MetricTag... tags) {
        return shouldReport(name, tags)
                ? underlying.gauge(name, tags)
                : emptyCollector.gauge(name, tags);
    }

    private boolean shouldReport(String name, MetricTag[] tags) {
        return reportLevel.ordinal() <= getLevel(name, tags).ordinal();
    }

    @Override
    public MetricLevel getLevel(String name, MetricTag... tags) {
        return underlying.getLevel(name, tags);
    }
}
