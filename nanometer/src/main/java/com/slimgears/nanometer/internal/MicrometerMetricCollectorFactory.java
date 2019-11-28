package com.slimgears.nanometer.internal;

import com.slimgears.nanometer.MetricCollector;
import com.slimgears.nanometer.MetricFilter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MicrometerMetricCollectorFactory extends AbstractMetricCollector.Factory {
    private final MeterRegistry registry;

    private MicrometerMetricCollectorFactory(MeterRegistry registry) {
        this.registry = registry;
    }

    public MicrometerMetricCollectorFactory bindMeterBinder(Iterable<MeterBinder> binders) {
        binders.forEach(b -> b.bindTo(registry));
        return this;
    }

    public MicrometerMetricCollectorFactory bindMeterBinder(MeterBinder binder) {
        binder.bindTo(registry);
        return this;
    }

    public MicrometerMetricCollectorFactory loadBindings() {
        ServiceLoader.load(MeterBinder.class, ClassLoader.getSystemClassLoader())
                .iterator()
                .forEachRemaining(this::bindMeterBinder);
        return this;
    }

    public static MicrometerMetricCollectorFactory forGlobal() {
        return forRegistry(Metrics.globalRegistry);
    }

    public static MicrometerMetricCollectorFactory forRegistry(MeterRegistry registry) {
        return new MicrometerMetricCollectorFactory(registry);
    }

    public static MicrometerMetricCollectorFactory forRegistries(Collection<MeterRegistry> registries) {
        return forRegistries(registries.toArray(new MeterRegistry[0]));
    }

    public static MicrometerMetricCollectorFactory forRegistries(MeterRegistry... registries) {
        if (registries.length == 0) {
            return forGlobal();
        } else if (registries.length == 1) {
            return forRegistry(registries[0]);
        }

        CompositeMeterRegistry compositeMeterRegistry = new CompositeMeterRegistry();
        Arrays.asList(registries).forEach(compositeMeterRegistry::add);
        return forRegistry(compositeMeterRegistry);
    }

    public static MicrometerMetricCollectorFactory forLoadableRegistries() {
        MeterRegistry[] registries = StreamSupport.stream(ServiceLoader
                .load(MeterRegistry.class, ClassLoader.getSystemClassLoader())
                .spliterator(), false)
                .toArray(MeterRegistry[]::new);
        return forRegistries(registries);
    }

    @Override
    protected MetricCollector create(MetricFilter filter) {
        return new MicrometerMetricCollector(registry, filter);
    }
}
