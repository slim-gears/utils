package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.util.autovalue.annotations.Key;

@AutoValuePrototype
public interface SampleWithKeyPrototype {
    @Key String id();
    String text();
    int number();
}
