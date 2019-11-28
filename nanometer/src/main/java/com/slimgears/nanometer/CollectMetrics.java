package com.slimgears.nanometer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CollectMetrics {
    String value();
    MetricLevel level() default MetricLevel.UNFILTERED;
    Tag[] tags() default {};

    @interface Tag {
        String key();
        String value();
    }
}
