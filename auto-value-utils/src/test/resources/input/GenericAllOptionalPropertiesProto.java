package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.UseCreatorMethods;
import com.slimgears.util.autovalue.annotations.UseMetaDataExtension;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

import javax.annotation.Nullable;

@CustomAutoAnnotation
public interface GenericAllOptionalPropertiesProto<T extends Comparable<T>> {
    @Nullable Integer intProperty();
    @Nullable String strProperty();
    @Nullable T genericValue();
}
