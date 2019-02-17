package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.AutoValueMetaData;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.util.autovalue.annotations.Key;

@AutoValuePrototype
@AutoValueMetaData
public interface SampleWithKeyPrototype {
    @Key String id();
    String text();
    int number();
}
