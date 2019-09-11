package com.slimgears.util.sample;

import com.slimgears.util.autovalue.annotations.AutoGeneric;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.util.autovalue.annotations.UseMetaDataExtension;

@AutoGeneric(
        className = "${K}${V}InterfacePrototype",
        variants = {
                @AutoGeneric.Variant({String.class, Integer.class})
        }
)
@AutoValuePrototype
@UseMetaDataExtension
public interface SampleAutoGenericAutoValuePrototype<K, V> {
    String name();
    K key();
    V value();
}
