package com.slimgears.sample.a;

import com.slimgears.util.autovalue.annotations.AutoValueMetaData;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

import javax.annotation.Nullable;

@AutoValuePrototype
@AutoValueMetaData
public interface SampleAPrototype {
    int value();
}
