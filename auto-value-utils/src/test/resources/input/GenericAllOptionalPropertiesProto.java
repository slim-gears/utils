package com.slimgears.sample;

import javax.annotation.Nullable;

@CustomAutoAnnotation
public interface GenericAllOptionalPropertiesProto<T extends Comparable<T>> {
    @Nullable Integer intProperty();
    @Nullable String strProperty();
    @Nullable T genericValue();
}
