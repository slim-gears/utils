package com.slimgears.sample;

import javax.annotation.Nullable;

@CustomAutoAnnotation
public interface SampleAllOptionalPropertiesProto {
    @Nullable Integer intProperty();
    @Nullable String strProperty();
}
