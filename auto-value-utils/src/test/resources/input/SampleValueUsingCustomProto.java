package com.slimgears.sample;

import javax.annotation.Nullable;

@CustomAutoAnnotation
interface SampleValueUsingCustomProto {
    int value();
    @Nullable Integer optionalValue();
}
