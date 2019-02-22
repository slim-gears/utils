package com.slimgears.sample;

import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
public interface SampleGenericPrototypeBuilder<T, _B extends SampleGenericPrototypeBuilder<T, _B>> extends SampleGenericInterface.Builder<T, _B>, SampleInterfaceBuilder<_B>, SampleGenericInterfaceBuilder<T, _B> {

    _B tValue(T tValue);

    _B value(String value);

}
