/**
 *
 */
package com.slimgears.apt.data;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.slimgears.apt.util.ImportTracker;
import com.slimgears.apt.util.JavaUtils;
import com.slimgears.apt.util.TemplateEvaluator;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import java.util.Arrays;
import java.util.Optional;

@AutoValue
public abstract class AnnotationInfo implements HasType {
    public abstract TypeInfo type();
    public abstract ImmutableList<AnnotationValueInfo> values();
    public boolean hasValues() {
        return !values().isEmpty();
    }
    public AnnotationValueInfo.Value getValue(String name) {
        return values()
                .stream()
                .filter(v -> name.equals(v.name()))
                .map(AnnotationValueInfo::value)
                .findFirst()
                .orElse(null);
    }

    public String asString() {
        ImportTracker tracker = Optional
                .ofNullable(ImportTracker.current())
                .orElseGet(ImportTracker::create);
        return asString(tracker);
    }

    public String asString(ImportTracker tracker) {
        return ImportTracker.withTracker(tracker, () -> TemplateEvaluator
                .forResource("annotation-value.java.vm")
                .apply(JavaUtils.imports(tracker))
                .variable("requiresExplicitName", values().size() != 1 || !"value".equals(values().get(0).name()))
                .variable("annotation", this)
                .evaluate());
    }

    public static Builder builder() {
        return new AutoValue_AnnotationInfo.Builder();
    }

    public static AnnotationInfo ofType(TypeInfo type, AnnotationValueInfo... values) {
        Builder builder = builder().type(type);
        builder.valuesBuilder().addAll(Arrays.asList(values));
        return builder.build();
    }

    public static AnnotationInfo ofType(String type, AnnotationValueInfo... values) {
        Builder builder = builder().type(TypeInfo.of(type));
        builder.valuesBuilder().addAll(Arrays.asList(values));
        return builder.build();
    }

    public static AnnotationInfo of(AnnotationMirror annotationMirror) {
        Builder builder = builder().type(annotationMirror.getAnnotationType());
        annotationMirror.getElementValues()
                .forEach((key, value) -> builder.value(key.getSimpleName().toString(), value));
        return builder.build();
    }

    @AutoValue.Builder
    public interface Builder extends HasType.Builder<Builder> {
        ImmutableList.Builder<AnnotationValueInfo> valuesBuilder();
        AnnotationInfo build();

        default Builder value(AnnotationValueInfo valueInfo) {
            valuesBuilder().add(valueInfo);
            return this;
        }

        default Builder value(String name, AnnotationValueInfo.Value value) {
            return value(AnnotationValueInfo.of(name, value));
        }

        default Builder value(String name, AnnotationValue annotationValue) {
            return value(AnnotationValueInfo.of(name, annotationValue));
        }
    }
}
