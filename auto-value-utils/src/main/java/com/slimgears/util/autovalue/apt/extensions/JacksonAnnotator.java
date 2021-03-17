package com.slimgears.util.autovalue.apt.extensions;

import com.google.auto.service.AutoService;
import com.slimgears.apt.data.AnnotationInfo;
import com.slimgears.apt.data.AnnotationValueInfo;
import com.slimgears.apt.data.TypeInfo;
import com.slimgears.util.autovalue.annotations.UseJacksonAnnotator;
import com.slimgears.util.autovalue.apt.Context;
import com.slimgears.util.autovalue.apt.PropertyInfo;
import com.slimgears.util.generic.MoreStrings;
import com.slimgears.util.stream.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.slimgears.util.generic.MoreStrings.lazy;
import static java.util.Collections.singleton;

@AutoService(Annotator.class)
@SupportedAnnotations(UseJacksonAnnotator.class)
public class JacksonAnnotator implements Annotator {
    @Override
    public Iterable<AnnotationInfo> annotatePropertyMethod(Context context, PropertyInfo property) {
//        return singleton(AnnotationInfo.ofType(
//                "com.fasterxml.jackson.annotation.JsonProperty",
//                AnnotationValueInfo.ofPrimitive("value", property.name())));

        List<AnnotationInfo> annotationInfos = property.annotations().stream()
                .filter(a -> a.type().packageName().startsWith("com.fasterxml.jackson.annotation") ||
                        a.type().simpleName().equals("Nullable") ||
                        a.type().simpleName().equals("NonNull"))
                .collect(Collectors.toList());

        TypeInfo jsonPropertyAnnotationType = TypeInfo.of("com.fasterxml.jackson.annotation.JsonProperty");
        if (!property.hasAnnotation(jsonPropertyAnnotationType)) {
            annotationInfos.add(AnnotationInfo.ofType(jsonPropertyAnnotationType,
                    AnnotationValueInfo.ofPrimitive("value", property.name())));
        }

        return annotationInfos;
    }

    @Override
    public Iterable<AnnotationInfo> annotateCreatorParameter(Context context, PropertyInfo property) {
        return Streams.fromIterable(annotatePropertyMethod(context, property))
                .filter(a -> a.supportsElement(ElementType.PARAMETER))
                .collect(Collectors.toList());
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
