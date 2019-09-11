package com.slimgears.util.sample;

import com.slimgears.util.autovalue.annotations.AutoGeneric;

import java.util.function.Function;

@AutoGeneric(
        className = "${T}${S}GenericInterface",
        variants = {
                @AutoGeneric.Variant({String.class, Integer.class}),
                @AutoGeneric.Variant({Integer.class, String.class})
        }
)
@CustomAnnotation(
        intValue = 1,
        strValue = "strValue",
        classValue = Function.class,
        nestedAnnotations = {
                @CustomAnnotation.NestedAnnotation("1"),
                @CustomAnnotation.NestedAnnotation("2")
        }
)
public interface SampleAutoGenericInterface<T, S> {
    S convert(T arg);
}
