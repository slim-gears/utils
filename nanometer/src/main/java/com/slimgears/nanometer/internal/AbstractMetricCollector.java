package com.slimgears.nanometer.internal;

import com.slimgears.nanometer.MetricCollector;
import com.slimgears.nanometer.MetricFilter;
import com.slimgears.nanometer.MetricLevel;
import com.slimgears.nanometer.MetricTag;

import java.util.concurrent.atomic.AtomicReference;

abstract class AbstractMetricCollector implements MetricCollector {
    private final MetricFilter filter;

    AbstractMetricCollector(MetricFilter filter) {
        this.filter = filter;
    }

    AbstractMetricCollector() {
        this((name, tags) -> MetricLevel.UNFILTERED);
    }

    @Override
    public MetricLevel getLevel(String name, MetricTag... tags) {
        return filter.getLevel(name, tags);
    }

    public static abstract class Factory implements MetricCollector.Factory {
        private final AtomicReference<MetricFilter> filter = new AtomicReference<>((name, tags) -> MetricLevel.UNFILTERED);

        @Override
        public MetricCollector create() {
            return create((name, tags) -> filter.get().getLevel(name, tags));
        }

        protected abstract MetricCollector create(MetricFilter filter);

        @Override
        public MetricCollector.Factory filter(MetricFilter filter) {
            this.filter.set(filter);
            return this;
        }
    }
}
