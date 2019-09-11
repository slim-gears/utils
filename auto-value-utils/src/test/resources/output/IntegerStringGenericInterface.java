package com.slimgears.util.sample;

import java.util.function.Function;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoGenericAnnotationProcessor")
@CustomAnnotation(intValue=1, strValue="strValue", classValue=Function.class, nestedAnnotations={@CustomAnnotation.NestedAnnotation("1"), @CustomAnnotation.NestedAnnotation("2")})
public interface IntegerStringGenericInterface extends SampleAutoGenericInterface<Integer, String> {
}
