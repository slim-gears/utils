package com.slimgears.nanometer.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.ProvidesIntoSet;
import com.slimgears.nanometer.InjectMetrics;
import com.slimgears.nanometer.MeterRegistryFactory;
import com.slimgears.nanometer.MetricCollector;
import com.slimgears.nanometer.internal.MicrometerMetricCollectorFactory;
import com.slimgears.util.guice.FieldInjector;
import com.slimgears.util.guice.ServiceModules;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;

import javax.inject.Provider;
import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NanometerModule extends AbstractModule {
    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface CompositeFactory {

    }

    @Override
    protected void configure() {
        install(ServiceModules.forServiceSet(MeterRegistryFactory.class));
        install(ServiceModules.forServiceSet(MeterRegistry.class));
        install(ServiceModules.forServiceSet(MeterBinder.class));
        install(ServiceModules.forServiceSet(MetricCollector.Binder.class));

        Provider<MetricCollector.Factory> compositeFactoryProvider = getProvider(Key.get(MetricCollector.Factory.class, CompositeFactory.class));
        FieldInjector.inject(MetricCollector.class)
                .toAnnotatedField(InjectMetrics.class)
                .resolveByClass(cls -> compositeFactoryProvider.get().createForClass(cls))
                .install(binder());
    }

    @ProvidesIntoSet
    private MeterRegistry provideFromFactories(Set<MeterRegistryFactory> factories) {
        List<MeterRegistry> registries = factories.stream()
                .map(MeterRegistryFactory::create)
                .collect(Collectors.toList());

        if (registries.size() == 1) {
            return registries.get(0);
        }

        CompositeMeterRegistry compositeMeterRegistry = new CompositeMeterRegistry();
        registries.forEach(compositeMeterRegistry::add);
        return compositeMeterRegistry;
    }

    @ProvidesIntoSet
    private MetricCollector.Factory provideMicrometerFactory(
            Set<MeterRegistry> meterRegistries,
            Set<MeterBinder> meterBinders) {
        return MicrometerMetricCollectorFactory
                .forRegistries(meterRegistries)
                .bindMeterBinder(meterBinders);
    }

    @Provides @Singleton @CompositeFactory
    private MetricCollector.Factory provideMetricCollectorFactory(
            Set<MetricCollector.Factory> factories,
            Set<MetricCollector.Binder> metricCollectorBinders) {
        return MetricCollector.Factory
                .composite(factories)
                .bind(metricCollectorBinders);
    }
}
