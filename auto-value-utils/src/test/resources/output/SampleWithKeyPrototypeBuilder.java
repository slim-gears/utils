package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.Key;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.extensions.BuilderExtension")
public interface SampleWithKeyPrototypeBuilder<_B extends SampleWithKeyPrototypeBuilder<_B>> {

    @Key
    _B id(String value);

    _B text(String value);

    _B number(int value);
    
}
