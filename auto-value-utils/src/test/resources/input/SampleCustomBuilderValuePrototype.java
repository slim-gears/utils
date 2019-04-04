package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.UseMetaDataExtension;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

@AutoValuePrototype
@UseMetaDataExtension
public interface SampleCustomBuilderValuePrototype {
    int intValue();
    double doubleValue();

    @AutoValuePrototype.Builder
    interface Builder<B extends Builder<B>> {
        B intValue(int val);
        default B fromString(String val) {
            return intValue(Integer.valueOf(val));
        }
    }
}
