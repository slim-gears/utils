/**
 *
 */
package com.slimgears.apt.data;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.slimgears.apt.util.TemplateEvaluator;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;

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
        return TemplateEvaluator
                .forResource("annotation-value.java.vm")
                .variable("annotation", this)
                .evaluate();
    }

    public static Builder builder() {
        return new AutoValue_AnnotationInfo.Builder();
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

        default Builder value(String name, AnnotationValue annotationValue) {
            valuesBuilder().add(AnnotationValueInfo.of(name, annotationValue));
            return this;
        }
    }
}
