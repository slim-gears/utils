package com.slimgears.sample;

import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
public interface SampleComparableGenericPrototypeBuilder<T extends Comparable<T>, _B extends SampleComparableGenericPrototypeBuilder<T, _B>> extends SampleComparableGenericInterfaceBuilder<T, _B> {

    _B tValue(T tValue);

}
