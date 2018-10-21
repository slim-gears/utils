package com.slimgears.sample;

import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
public interface SampleValuePrototypeBuilder<_B extends SampleValuePrototypeBuilder<_B>> {

    _B intValue(int intValue);

    @SampleFieldAnnotation(strValue = "test")
    _B doubleValue(double doubleValue);

    @SampleFieldAnnotation
    _B strValue(String strValue);

    _B foo(boolean foo);

}
