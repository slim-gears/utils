package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.UseMetaDataExtension;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

@AutoValuePrototype
@UseMetaDataExtension
public interface SampleSpecializedGenericPrototype extends SampleGuavaInterface<String> {
}
