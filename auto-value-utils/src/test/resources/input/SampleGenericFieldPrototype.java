package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.AutoValueMetaData;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

import java.util.Collection;

@AutoValuePrototype
@AutoValueMetaData
public interface SampleGenericFieldPrototype {
    Class<? extends Collection> value();
}
