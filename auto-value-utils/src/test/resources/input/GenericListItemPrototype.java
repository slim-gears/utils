package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

@AutoValuePrototype()
public interface GenericListItemPrototype<T> {
    T entry();
}
