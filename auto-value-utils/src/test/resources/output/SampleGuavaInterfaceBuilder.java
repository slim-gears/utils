package com.slimgears.sample;

import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.extensions.BuilderExtension")
public interface SampleGuavaInterfaceBuilder<T, _B extends SampleGuavaInterfaceBuilder<T, _B>> {

    _B values(ImmutableList<T> values);
    
    ImmutableList.Builder<T> valuesBuilder();
        
    default _B valuesAdd(T element) {
        valuesBuilder().add(element); 
        return (_B)this;
    }

    default _B valuesAdd(T... elements) {
        valuesBuilder().add(elements); 
        return (_B)this;
    }

    default _B valuesAddAll(Iterable<? extends T> elements) {
        valuesBuilder().addAll(elements); 
        return (_B)this;
    }

    default _B valuesAddAll(Iterator<? extends T> elements) {
        valuesBuilder().addAll(elements); 
        return (_B)this;
    }

}
