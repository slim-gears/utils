package com.slimgears.apt.data;

import com.google.auto.value.AutoValue;

import javax.lang.model.element.VariableElement;

@AutoValue
public abstract class ParamInfo implements HasName, HasType, HasAnnotations {
    public static ParamInfo of(VariableElement element) {
        return builder()
                .name(element.getSimpleName().toString())
                .type(TypeInfo.of(element.asType().toString()))
                .annotationsFromElement(element)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_ParamInfo.Builder();
    }

    public static ParamInfo create(String name, TypeInfo type) {
        return builder().name(name).type(type).build();
    }

    @AutoValue.Builder
    public interface Builder extends
            InfoBuilder<ParamInfo>,
            HasName.Builder<Builder>,
            HasType.Builder<Builder>,
            HasAnnotations.Builder<Builder> {
    }
}
