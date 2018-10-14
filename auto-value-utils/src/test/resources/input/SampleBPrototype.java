package com.slimgears.sample.b;

import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

import javax.annotation.Nullable;

@AutoValuePrototype()
public interface SampleBPrototype {
    com.slimgears.sample.a.SampleA value();
}
