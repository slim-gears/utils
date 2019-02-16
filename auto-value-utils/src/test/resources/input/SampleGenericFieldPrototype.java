package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.AutoValueExpressions;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

import java.util.Collection;

@AutoValuePrototype
@AutoValueExpressions
public interface SampleGenericFieldPrototype {
    Class<? extends Collection> value();
}
