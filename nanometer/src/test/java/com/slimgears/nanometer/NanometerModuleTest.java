package com.slimgears.nanometer;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.ProvidesIntoSet;
import com.slimgears.nanometer.guice.NanometerModule;
import com.slimgears.util.guice.InjectLog;
import com.slimgears.util.guice.LogModule;
import com.slimgears.util.test.guice.GuiceJUnit;
import com.slimgears.util.test.guice.UseModules;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import javax.inject.Inject;

@RunWith(GuiceJUnit.class)
@UseModules({NanometerModule.class, LogModule.class, NanometerModuleTest.TestModule.class})
@CollectMetrics(value = "nanometer.test")
public class NanometerModuleTest {
    @InjectLog Logger log;
    @InjectMetrics MetricCollector metricCollector;
    @Inject SimpleMeterRegistry simpleMeterRegistry;

    public static class TestModule extends AbstractModule {
        @Provides @Singleton
        private SimpleMeterRegistry provideSimpleMeterRegistry() {
            return new SimpleMeterRegistry();
        }

        @ProvidesIntoSet
        private MeterRegistry provideSimpleMeterRegistry(SimpleMeterRegistry simpleMeterRegistry) {
            return simpleMeterRegistry;
        }
    }

    @Test
    public void testInjectedMetrics() {
        Assert.assertNotNull(metricCollector);
        metricCollector.counter("testCounter").increment();
        Assert.assertNotNull(simpleMeterRegistry.find("nanometer.test.testCounter").counter());
        Assert.assertEquals(1.0, simpleMeterRegistry.find("nanometer.test.testCounter").counter().count(), 0.01);
    }

    @Test
    public void testInjectLogger() {
        Assert.assertNotNull(log);
        log.info("Hello, World");
    }
}
