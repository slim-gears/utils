package com.slimgears.nanometer.internal;

import com.slimgears.nanometer.MetricCollector;
import com.slimgears.nanometer.MetricFilter;
import com.slimgears.nanometer.MetricLevel;

import java.util.Collection;
import java.util.stream.Collectors;

public class CompositeMetricCollectorFactory implements MetricCollector.Factory {
    private final Collection<MetricCollector.Factory> factories;

    private CompositeMetricCollectorFactory(Collection<MetricCollector.Factory> factories) {
        this.factories = factories;
    }

    public static MetricCollector.Factory create(Collection<MetricCollector.Factory> factories) {
        if (factories.isEmpty()) {
            return EmptyMetricCollector.factory;
        } else if (factories.size() == 1) {
            return factories.iterator().next();
        } else {
            return new CompositeMetricCollectorFactory(factories);
        }
    }

    @Override
    public MetricCollector create() {
        Collection<MetricCollector> metricCollectors = factories.stream()
                .map(MetricCollector.Factory::create)
                .collect(Collectors.toList());
        return CompositeMetricCollector.of(metricCollectors);
    }

    @Override
    public MetricCollector.Factory filter(MetricFilter filter) {
        factories.forEach(f -> f.filter(filter));
        return this;
    }
}
