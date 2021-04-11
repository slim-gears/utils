package com.slimgears.util.test;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface AnnotationRuleProvider<A extends Annotation> extends AdditionalInfoRuleProvider<A> {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.ANNOTATION_TYPE)
    @interface Qualifier {
        Class<? extends AnnotationRuleProvider<? extends Annotation>> value();
    }
}
