/**
 *
 */
package com.slimgears.apt.data;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;

import javax.lang.model.element.AnnotationMirror;

@AutoValue
public abstract class AnnotationInfo implements HasType {
    public abstract TypeInfo type();
    public abstract ImmutableMap<String, Object> values();

    public static Builder builder() {
        return new AutoValue_AnnotationInfo.Builder();
    }

    public static AnnotationInfo of(AnnotationMirror annotationMirror) {
        Builder builder = builder().type(annotationMirror.getAnnotationType());
        annotationMirror.getElementValues().forEach((key, value) -> builder.value(key.getSimpleName().toString(), value.getValue()));
        return builder.build();
    }

    @AutoValue.Builder
    public interface Builder extends HasType.Builder<Builder> {
        ImmutableMap.Builder<String, Object> valuesBuilder();
        AnnotationInfo build();

        default Builder value(String name, Object val) {
            valuesBuilder().put(name, val);
            return this;
        }
    }
}
