package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.UseMetaDataExtension;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.util.autovalue.annotations.Key;

@AutoValuePrototype
@UseMetaDataExtension
public interface SampleWithKeyPrototype {
    @Key String id();
    String text();
    int number();
}
