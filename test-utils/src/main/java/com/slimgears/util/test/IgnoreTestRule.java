package com.slimgears.util.test;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

public interface IgnoreTestRule<A extends Annotation> {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.ANNOTATION_TYPE)
    @interface Qualifier {
        Class<? extends IgnoreTestRule<? extends Annotation>> value();
    }

    boolean isIgnored(A annotation, Method method);
}
