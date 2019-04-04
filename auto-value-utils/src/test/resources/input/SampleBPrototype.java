package com.slimgears.sample.b;

import com.slimgears.util.autovalue.annotations.UseMetaDataExtension;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.sample.a.SampleA;
import com.slimgears.sample.a.SampleAPrototype;

@AutoValuePrototype
@UseMetaDataExtension
public interface SampleBPrototype {
    SampleA value();
    SampleAPrototype.NestedEnum nestedEnum();
}
