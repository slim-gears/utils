package com.slimgears.nanometer.internal;

import com.slimgears.nanometer.CollectMetrics;
import com.slimgears.nanometer.MetricCollector;
import com.slimgears.nanometer.MetricTag;

import java.util.Arrays;
import java.util.Optional;

public class MetricAnnotations {
    public static MetricCollector decorate(MetricCollector collector, Class<?> clazz) {
        return Optional.ofNullable(clazz.getAnnotation(CollectMetrics.class))
                .map(annotation -> decorate(collector, annotation))
                .orElse(collector);
    }

    private static MetricCollector decorate(MetricCollector collector, CollectMetrics annotation) {
        return collector
                .tags(toTags(annotation.tags()))
                .name(annotation.value())
                .level(annotation.level());
    }

    private static MetricTag[] toTags(CollectMetrics.Tag[] tags) {
        if (tags.length == 0) {
            return new MetricTag[0];
        }
        return Arrays.stream(tags)
                .map(t -> MetricTag.of(t.key(), t.value()))
                .toArray(MetricTag[]::new);
    }
}
