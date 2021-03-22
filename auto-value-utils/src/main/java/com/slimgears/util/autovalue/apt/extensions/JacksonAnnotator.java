package com.slimgears.util.autovalue.apt.extensions;

import com.google.auto.service.AutoService;
import com.google.common.base.Strings;
import com.slimgears.apt.data.AnnotationInfo;
import com.slimgears.apt.data.AnnotationValueInfo;
import com.slimgears.apt.data.TypeInfo;
import com.slimgears.util.autovalue.annotations.UseJacksonAnnotator;
import com.slimgears.util.autovalue.apt.Context;
import com.slimgears.util.autovalue.apt.PropertyInfo;
import com.slimgears.util.stream.Streams;

import java.lang.annotation.ElementType;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.singleton;

@AutoService(Annotator.class)
@SupportedAnnotations(UseJacksonAnnotator.class)
public class JacksonAnnotator implements Annotator {
    @Override
    public Iterable<AnnotationInfo> annotatePropertyMethod(Context context, PropertyInfo property) {
        TypeInfo jsonPropertyAnnotationType = TypeInfo.of("com.fasterxml.jackson.annotation.JsonProperty");
        Map<TypeInfo, AnnotationInfo> annotationInfos = property.annotations().stream()
                .filter(a -> a.type().packageName().startsWith("com.fasterxml.jackson.annotation") ||
                        a.type().simpleName().equals("Nullable") ||
                        a.type().simpleName().equals("NonNull"))
                .collect(Collectors.toMap(AnnotationInfo::type, a -> a));

        boolean hasExplicitPropertyNameOptional = Optional
                .ofNullable(annotationInfos.get(jsonPropertyAnnotationType))
                .map(a -> a.getValue("value"))
                .map(AnnotationValueInfo.Value::primitive)
                .map(String::valueOf)
                .map(s -> !Strings.isNullOrEmpty(s))
                .orElse(false);

        if (!hasExplicitPropertyNameOptional) {
            annotationInfos.put(jsonPropertyAnnotationType, AnnotationInfo.ofType(jsonPropertyAnnotationType,
                    AnnotationValueInfo.ofPrimitive("value", property.name())));
        }

        return annotationInfos.values();
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
