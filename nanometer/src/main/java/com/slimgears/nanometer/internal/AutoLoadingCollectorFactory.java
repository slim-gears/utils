package com.slimgears.nanometer.internal;

import com.slimgears.nanometer.MetricCollector;
import com.slimgears.nanometer.MetricFilter;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AutoLoadingCollectorFactory implements MetricCollector.Factory {
    private final AtomicReference<MetricCollector.Factory> factory = new AtomicReference<>();
    private final Supplier<MetricCollector.Factory> defaultFactory;

    private AutoLoadingCollectorFactory(Supplier<MetricCollector.Factory> defaultFactory) {
        this.defaultFactory = defaultFactory;
    }

    public static MetricCollector.Factory newInstance(Supplier<MetricCollector.Factory> defaultFactory) {
        return new AutoLoadingCollectorFactory(defaultFactory);
    }

    public static MetricCollector.Factory newInstance() {
        return newInstance(() -> EmptyMetricCollector.factory);
    }

    @Override
    public MetricCollector create() {
        return factory().create();
    }

    @Override
    public MetricCollector.Factory filter(MetricFilter filter) {
        factory().filter(filter);
        return this;
    }

    private MetricCollector.Factory factory() {
        return Optional.ofNullable(factory.get())
                .orElseGet(this::createInstance);
    }

    private synchronized MetricCollector.Factory createInstance() {
        MetricCollector.Factory factoryInstance = factory.get();
        if (factoryInstance != null) {
            return factoryInstance;
        }

        List<MetricCollector.Factory> foundFactories = StreamSupport
                .stream(ServiceLoader.load(MetricCollector.Factory.class, ClassLoader.getSystemClassLoader()).spliterator(), false)
                .collect(Collectors.toList());

        factoryInstance = foundFactories.isEmpty()
                ? defaultFactory.get()
                : CompositeMetricCollectorFactory.create(foundFactories);

        factory.set(factoryInstance);
        return factoryInstance;
    }
}
