package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

import java.util.List;

@AutoValuePrototype
public interface GenericListContainerPrototype<T> {
    List<com.slimgears.sample.GenericListItem<T>> items();
}
