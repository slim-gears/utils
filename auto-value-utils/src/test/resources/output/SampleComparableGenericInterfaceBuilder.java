package com.slimgears.sample;

import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
public interface SampleComparableGenericInterfaceBuilder<T extends Comparable<T>, _B extends SampleComparableGenericInterfaceBuilder<T, _B>> {

    _B tValue(T tValue);
    
}
