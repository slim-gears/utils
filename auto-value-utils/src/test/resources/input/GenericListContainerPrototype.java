package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.AutoValueExpressions;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

import java.util.List;

@AutoValuePrototype
@AutoValueExpressions
public interface GenericListContainerPrototype<T> {
    List<com.slimgears.sample.GenericListItem<T>> items();
}
