package com.slimgears.util.autovalue.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface AutoValuePrototype {
    String value() default "";
    String pattern() default "";
    String[] extensions() default {};
    String[] annotators() default {};

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    @interface Builder {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.ANNOTATION_TYPE)
    @interface Extension {
        String[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.ANNOTATION_TYPE)
    @interface Annotator {
        String[] value();
    }
}
