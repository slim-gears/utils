package com.slimgears.util.sample;

import com.slimgears.util.autovalue.annotations.AutoGeneric;
import java.util.function.Function;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoGenericAnnotationProcessor")
@AutoGeneric.Variant({String.class, Integer.class})
@CustomAnnotation(intValue=1, strValue="strValue", classValue=Function.class, nestedAnnotations={@CustomAnnotation.NestedAnnotation("1"), @CustomAnnotation.NestedAnnotation("2")})
public interface StringIntegerGenericInterface extends SampleAutoGenericInterface<String, Integer> {
}
