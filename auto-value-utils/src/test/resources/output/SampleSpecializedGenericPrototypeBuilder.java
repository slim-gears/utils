package com.slimgears.sample;

import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
public interface SampleSpecializedGenericPrototypeBuilder<_B extends SampleSpecializedGenericPrototypeBuilder<_B>> extends SampleGuavaInterfaceBuilder<java.lang.String, _B> {

    _B values(ImmutableList<String> values);

    ImmutableList.Builder<String> valuesBuilder();

    default _B valuesAdd(String element) {
        valuesBuilder().add(element);
        return (_B)this;
    }

    default _B valuesAdd(String... elements) {
        valuesBuilder().add(elements);
        return (_B)this;
    }

    default _B valuesAddAll(Iterable<? extends String> elements) {
        valuesBuilder().addAll(elements);
        return (_B)this;
    }

    default _B valuesAddAll(Iterator<? extends String> elements) {
        valuesBuilder().addAll(elements);
        return (_B)this;
    }

}
