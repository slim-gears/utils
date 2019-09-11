package com.slimgears.util.sample;

import com.slimgears.util.autovalue.annotations.AutoGeneric;

import javax.inject.Inject;
import java.util.function.Function;

@AutoGeneric(
        className = "${T}${S}GenericClass",
        variants = {
                @AutoGeneric.Variant({String.class, Integer.class}),
                @AutoGeneric.Variant({Integer.class, String.class})
        }
)
public class SampleAutoGenericClass<T, S> {
    private final Class<T> fromClass;
    private final Class<S> toClass;
    private final Function<T, S> converter;

    @Inject
    public SampleAutoGenericClass(@AutoGeneric.ClassParam("T") Class<T> fromClass,
                              @AutoGeneric.ClassParam("S") Class<S> toClass,
                              Function<T, S> converter) {
        this.fromClass = fromClass;
        this.toClass = toClass;
        this.converter = converter;
    }

    public S convert(T arg) {
        return converter.apply(arg);
    }
}
