package com.slimgears.sample;

import com.google.common.collect.ImmutableList;

public interface SampleGuavaInterface<T> {
    ImmutableList<T> values();
}
