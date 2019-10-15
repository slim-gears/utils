package com.slimgears.util.sample;

import com.slimgears.util.autovalue.annotations.AutoGeneric;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.inject.Inject;

@Generated("com.slimgears.util.autovalue.apt.AutoGenericAnnotationProcessor")
@AutoGeneric.Variant({Integer.class, String.class})
public class IntegerStringGenericClass extends SampleAutoGenericClass<Integer, String> {
    @Inject
    public IntegerStringGenericClass(Function<Integer, String> converter) {
        super(Integer.class, String.class, converter);
    }
}
