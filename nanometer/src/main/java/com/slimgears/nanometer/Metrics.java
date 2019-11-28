package com.slimgears.nanometer;

import com.slimgears.nanometer.internal.AutoLoadingCollectorFactory;
import com.slimgears.nanometer.internal.MicrometerMetricCollectorFactory;

public class Metrics {
    private static final MetricCollector.Factory factory = AutoLoadingCollectorFactory
            .newInstance(() -> MicrometerMetricCollectorFactory.forLoadableRegistries().loadBindings())
            .autoBind();

    public static MetricCollector collector(Class<?> cls) {
        return factory.createForClass(cls);
    }

    public static void filter(MetricFilter filter) {
        factory.filter(filter);
    }

    public static void level(MetricLevel level) {
        factory.level(level);
    }
}
