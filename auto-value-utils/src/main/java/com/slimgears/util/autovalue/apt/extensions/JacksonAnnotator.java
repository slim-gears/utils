package com.slimgears.util.autovalue.apt.extensions;

import com.google.auto.service.AutoService;
import com.slimgears.apt.data.AnnotationInfo;
import com.slimgears.apt.data.AnnotationValueInfo;
import com.slimgears.apt.data.TypeInfo;
import com.slimgears.util.autovalue.annotations.UseJacksonAnnotator;
import com.slimgears.util.autovalue.apt.Context;
import com.slimgears.util.autovalue.apt.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singleton;

@AutoService(Annotator.class)
@SupportedAnnotations(UseJacksonAnnotator.class)
public class JacksonAnnotator implements Annotator {
    @Override
    public Iterable<AnnotationInfo> annotatePropertyMethod(Context context, PropertyInfo property) {
        List<AnnotationInfo> annotationInfos = new ArrayList<>(property.annotations());
        TypeInfo jsonPropertyAnnotationType = TypeInfo.of("com.fasterxml.jackson.annotation.JsonProperty");
        if (!property.hasAnnotation(jsonPropertyAnnotationType)) {
            annotationInfos.add(AnnotationInfo.ofType(jsonPropertyAnnotationType,
                    AnnotationValueInfo.ofPrimitive("value", property.name())));
        }
        return annotationInfos;
    }

    @Override
    public Iterable<AnnotationInfo> annotateCreatorParameter(Context context, PropertyInfo property) {
        return annotatePropertyMethod(context, property);
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
