package com.slimgears.util.autovalue.apt.extensions;

import com.slimgears.apt.data.AnnotationInfo;
import com.slimgears.util.autovalue.apt.Context;
import com.slimgears.util.autovalue.apt.PropertyInfo;

import static com.slimgears.apt.data.AnnotationValueInfo.ofPrimitive;
import static java.util.Collections.singleton;

public class JacksonAnnotator implements Annotator {
    @Override
    public Iterable<AnnotationInfo> annotatePropertyMethod(Context context, PropertyInfo property) {
        return singleton(AnnotationInfo.ofType(
                "com.fasterxml.jackson.annotation.JsonProperty"));
    }

    @Override
    public Iterable<AnnotationInfo> annotateCreatorParameter(Context context, PropertyInfo property) {
        return singleton(AnnotationInfo.ofType(
                "com.fasterxml.jackson.annotation.JsonProperty",
                ofPrimitive("value", property.name())));
    }

    @Override
    public Iterable<AnnotationInfo> annotateCreator(Context context) {
        return singleton(AnnotationInfo.ofType("com.fasterxml.jackson.annotation.JsonCreator"));
    }

    @Override
    public Iterable<AnnotationInfo> annotateNonPropertyMethod(Context context) {
        return singleton(AnnotationInfo.ofType("com.fasterxml.jackson.annotation.JsonIgnore"));
    }
}
