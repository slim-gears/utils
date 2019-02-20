package com.slimgears.util.repository.query;

import com.slimgears.util.repository.expressions.ObjectExpression;

public interface HasMapping<S, T> {
    ObjectExpression<S, T> mapping();
}
