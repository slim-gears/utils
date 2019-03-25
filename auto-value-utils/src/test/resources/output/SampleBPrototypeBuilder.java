package com.slimgears.sample.b;

import com.slimgears.sample.a.SampleA;
import com.slimgears.sample.a.SampleAPrototype;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
public interface SampleBPrototypeBuilder<_B extends SampleBPrototypeBuilder<_B>> {

    _B nestedEnum(SampleAPrototype.NestedEnum nestedEnum);

    _B value(SampleA value);

    SampleA.Builder valueBuilder();

    default _B valueValue(int value) {
        valueBuilder().value(value);
        return (_B)this;
    }

}
