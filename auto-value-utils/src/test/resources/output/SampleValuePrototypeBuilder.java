package com.slimgears.sample;

import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.extensions.BuilderExtension")
public interface SampleValuePrototypeBuilder<_B extends SampleValuePrototypeBuilder<_B>> {

    _B intValue(int value);

    @SampleFieldAnnotation(strValue = "test")
    _B doubleValue(double value);

    @SampleFieldAnnotation
    _B strValue(String value);

    _B foo(boolean value);
    
}
