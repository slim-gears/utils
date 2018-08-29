package com.slimgears.util.sample;

import java.util.function.Function;
import javax.annotation.Generated;
import javax.inject.Inject;

@Generated("com.slimgears.util.generic.AutoGenericAnnotationProcessor")
public class IntegerStringGenericClass extends SampleGenericClass<Integer, String> {
    @Inject
    public IntegerStringGenericClass(Function<Integer, String> converter) {
        super(Integer.class, String.class, converter);
    }
}
