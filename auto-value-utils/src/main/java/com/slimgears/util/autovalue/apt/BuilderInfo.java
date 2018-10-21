package com.slimgears.util.autovalue.apt;

import com.google.auto.value.AutoValue;
import com.slimgears.apt.data.TypeInfo;

import javax.lang.model.type.DeclaredType;

@AutoValue
public abstract class BuilderInfo {
    public abstract TypeInfo valueType();
    public abstract TypeInfo builderType();

    public static BuilderInfo create(DeclaredType valueType, DeclaredType builderType) {
        return create(TypeInfo.of(valueType), TypeInfo.of(builderType));
    }

    public static BuilderInfo create(TypeInfo valueType, TypeInfo builderType) {
        return new AutoValue_BuilderInfo(valueType, builderType);
    }
}
