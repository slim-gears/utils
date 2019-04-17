package com.slimgears.sample;

import com.google.common.collect.ImmutableList;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

@AutoValuePrototype
public interface SampleList<T> {
    ImmutableList<T> list();
}
