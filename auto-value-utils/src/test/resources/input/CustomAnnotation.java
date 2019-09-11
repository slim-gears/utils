package com.slimgears.util.sample;

@interface CustomAnnotation {
    int intValue() default 0;
    String strValue() default "";
    NestedAnnotation[] nestedAnnotations() default {};
    Class<?> classValue() default Object.class;

    @interface NestedAnnotation {
        String value();
    }
}
