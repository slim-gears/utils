package com.slimgears.util.autovalue.apt.extensions;

import com.google.common.collect.Sets;
import com.slimgears.apt.data.AnnotationInfo;
import com.slimgears.util.autovalue.apt.Context;
import com.slimgears.util.autovalue.apt.PropertyInfo;
import com.slimgears.util.stream.Streams;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CompositeAnnotator implements Annotator {
    private final Collection<Annotator> annotators;

    private CompositeAnnotator(Collection<Annotator> annotators) {
        this.annotators = annotators;
    }

    public static CompositeAnnotator of(Annotator... annotators) {
        return of(Arrays.asList(annotators));
    }

    public static CompositeAnnotator of(Collection<Annotator> annotators) {
        return new CompositeAnnotator(annotators);
    }

    @Override
    public Iterable<AnnotationInfo> annotatePropertyMethod(Context context, PropertyInfo property) {
        return combine(annotator -> annotator.annotatePropertyMethod(context, property));
    }

    @Override
    public Iterable<AnnotationInfo> annotateCreatorParameter(Context context, PropertyInfo property) {
        return combine(annotator -> annotator.annotateCreatorParameter(context, property));
    }

    @Override
    public Iterable<AnnotationInfo> annotateCreator(Context context) {
        return combine(annotator -> annotator.annotateCreator(context));
    }

    @Override
    public Iterable<AnnotationInfo> annotateClass(Context context) {
        return combine(annotator -> annotator.annotateClass(context));
    }

    @Override
    public Iterable<AnnotationInfo> annotateBuilderClass(Context context) {
        return combine(annotator -> annotator.annotateBuilderClass(context));
    }

    @Override
    public Iterable<AnnotationInfo> annotateNonPropertyMethod(Context context) {
        return combine(annotator -> annotator.annotateNonPropertyMethod(context));
    }

    private Iterable<AnnotationInfo> combine(Function<Annotator, Iterable<AnnotationInfo>> func) {
        return annotators.stream()
                .flatMap(a -> Streams.fromIterable(func.apply(a)))
                .collect(Collectors.groupingBy(AnnotationInfo::type, LinkedHashMap::new, Collectors.toCollection(Sets::newLinkedHashSet)))
                .entrySet()
                .stream()
                .map(g -> g.getValue().stream().findFirst().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
