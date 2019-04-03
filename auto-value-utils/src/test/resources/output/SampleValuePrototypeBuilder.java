package com.slimgears.sample;

import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
public interface SampleValuePrototypeBuilder<_B extends SampleValuePrototypeBuilder<_B>> {

    @SampleFieldAnnotation(strValue = "test")
    _B doubleValue(double doubleValue);

    _B foo(boolean foo);

    _B intValue(int intValue);

    @SampleFieldAnnotation
    _B strValue(String strValue);
    
}
