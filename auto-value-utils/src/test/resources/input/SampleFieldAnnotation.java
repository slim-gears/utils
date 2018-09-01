package com.slimgears.sample;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@interface SampleFieldAnnotation {
    String strValue() default "";
    int value() default 1;
    long longValue() default 2l;
}
