package com.slimgears.nanometer.internal;

import com.slimgears.nanometer.MetricCollector;
import com.slimgears.nanometer.MetricLevel;
import com.slimgears.nanometer.MetricTag;
import com.slimgears.nanometer.MetricTags;

public class WithNameAndTagsMetricCollectorDecorator implements MetricCollector {
    private final MetricCollector underlying;
    private final String name;
    private final MetricTags tags;

    private WithNameAndTagsMetricCollectorDecorator(MetricCollector underlying, String name, MetricTag... tags) {
        if (underlying instanceof WithNameAndTagsMetricCollectorDecorator) {
            WithNameAndTagsMetricCollectorDecorator underlyingDecorator = (WithNameAndTagsMetricCollectorDecorator)underlying;
            underlying = underlyingDecorator.underlying;
            tags = underlyingDecorator.tags.combineWith(tags);
            name = combineNames(underlyingDecorator.name, name);
        }

        this.underlying = underlying;
        this.tags = MetricTags.of(tags);
        this.name = name.isEmpty() ? name : name + ".";
    }

    public static MetricCollector decorate(MetricCollector underlying, String name, MetricTag... tags) {
        if (name.isEmpty() && tags.length == 0) {
            return underlying;
        }

        return new WithNameAndTagsMetricCollectorDecorator(underlying, name, tags);
    }

    @Override
    public Counter counter(String name, MetricTag... tags) {
        return underlying.counter(this.name + name, this.tags.combineWith(tags));
    }

    @Override
    public Timer timer(String name, MetricTag... tags) {
        return underlying.timer(this.name + name, this.tags.combineWith(tags));
    }

    @Override
    public Gauge gauge(String name, MetricTag... tags) {
        return underlying.gauge(this.name + name, this.tags.combineWith(tags));
    }

    @Override
    public MetricLevel getLevel(String name, MetricTag... tags) {
        return underlying.getLevel(this.name + name, this.tags.combineWith(tags));
    }

    private static String combineNames(String first, String second) {
        if (second.isEmpty()) {
            return first;
        }
        if (first.isEmpty()) {
            return second;
        }
        return first + second;
    }
}
