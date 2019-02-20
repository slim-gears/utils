package com.slimgears.util.repository.query;

import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.util.repository.expressions.PropertyExpression;

@AutoValuePrototype
public interface SortingInfoPrototype<S, V> {
    PropertyExpression<S, ?, ?, V> property();
    boolean ascending();
}
