package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.AutoValueExpressions;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.util.autovalue.annotations.Key;

@AutoValuePrototype
@AutoValueExpressions
public interface SampleWithKeyPrototype {
    @Key String id();
    String text();
    int number();
}
