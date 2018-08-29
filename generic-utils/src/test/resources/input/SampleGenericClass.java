package com.slimgears.util.sample;

import com.slimgears.util.generic.AutoGeneric;

import javax.inject.Inject;
import java.util.function.Function;

@AutoGeneric(
        className = "${T}${S}GenericClass",
        implementations = {
                @AutoGeneric.WithParams({String.class, Integer.class}),
                @AutoGeneric.WithParams({Integer.class, String.class})
        }
)
public class SampleGenericClass<T, S> {
    private final Class<T> fromClass;
    private final Class<S> toClass;
    private final Function<T, S> converter;

    @Inject
    public SampleGenericClass(@AutoGeneric.ClassParam("T") Class<T> fromClass,
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
