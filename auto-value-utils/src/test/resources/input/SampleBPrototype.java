package com.slimgears.sample.b;

import com.slimgears.util.autovalue.annotations.AutoValueExpressions;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.sample.a.SampleA;

import javax.annotation.Nullable;

@AutoValuePrototype
@AutoValueExpressions
public interface SampleBPrototype {
    SampleA value();
}
