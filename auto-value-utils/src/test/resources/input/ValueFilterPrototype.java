package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.util.autovalue.annotations.UseAutoValueAnnotator;
import com.slimgears.util.autovalue.annotations.UseBuilderExtension;
import com.slimgears.util.autovalue.annotations.UseMetaDataExtension;
import com.slimgears.sample.ObjectFilterPrototype;

import javax.annotation.Nullable;

@AutoValuePrototype
@UseMetaDataExtension
public interface ValueFilterPrototype<T> extends ObjectFilterPrototype<T> {
    @Nullable T equalsTo();
}
