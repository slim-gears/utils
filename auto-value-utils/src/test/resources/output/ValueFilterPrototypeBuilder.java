package com.slimgears.sample;

import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.extensions.BuilderExtension")
public interface ValueFilterPrototypeBuilder<T, _B extends ValueFilterPrototypeBuilder<T, _B>> extends ObjectFilterPrototypeBuilder<T, _B> {

    _B isNull(Boolean value);

    _B equalsTo(T value);
    
}
