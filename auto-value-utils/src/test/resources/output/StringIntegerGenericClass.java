package com.slimgears.util.sample;

import java.util.function.Function;
import javax.annotation.Generated;
import javax.inject.Inject;

@Generated("com.slimgears.util.autovalue.apt.AutoGenericAnnotationProcessor")
public class StringIntegerGenericClass extends SampleAutoGenericClass<String, Integer> {
    @Inject
    public StringIntegerGenericClass(Function<String, Integer> converter) {
        super(String.class, Integer.class, converter);
    }
}
