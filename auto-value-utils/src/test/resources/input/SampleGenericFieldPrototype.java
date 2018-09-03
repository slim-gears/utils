package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

import java.util.Collection;

@AutoValuePrototype
public interface SampleGenericFieldPrototype {
    Class<? extends Collection> value();
}
