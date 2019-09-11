package com.slimgears.util.sample;

import java.util.function.Function;
import javax.annotation.Generated;
import javax.inject.Inject;

@Generated("com.slimgears.util.autovalue.apt.AutoGenericAnnotationProcessor")
public class IntegerStringGenericClass extends SampleAutoGenericClass<Integer, String> {
    @Inject
    public IntegerStringGenericClass(Function<Integer, String> converter) {
        super(Integer.class, String.class, converter);
    }
}
