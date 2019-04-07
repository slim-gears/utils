package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.UseMetaDataExtension;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

@AutoValuePrototype
@UseMetaDataExtension
public interface SampleGenericPrototype<T> extends SampleInterface, SampleGenericInterface<T> {
    static <T> SampleGenericPrototype<T> createDefault(T value) {
        return SampleGeneric.<T>builder().tValue(value).build();
    }
}
