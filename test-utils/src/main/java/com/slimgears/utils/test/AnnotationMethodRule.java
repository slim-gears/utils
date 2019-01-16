package com.slimgears.utils.test;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface AnnotationMethodRule<A extends Annotation> extends AdditionalInfoMethodRule<A> {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.ANNOTATION_TYPE)
    @interface Qualifier {
        Class<? extends AnnotationMethodRule<? extends Annotation>> value();
    }
}
