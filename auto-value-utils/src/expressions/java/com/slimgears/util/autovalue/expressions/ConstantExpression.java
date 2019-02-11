package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slimgears.util.autovalue.expressions.internal.NumericConstantExpression;
import com.slimgears.util.autovalue.expressions.internal.ObjectConstantExpression;
import com.slimgears.util.autovalue.expressions.internal.StringConstantExpression;

public interface ConstantExpression<T> extends ObjectExpression<T> {
    @JsonProperty T value();

    static <V> ConstantExpression<V> of(V value) {
        return ObjectConstantExpression.create(Type.Constant, value);
    }

    static <V extends Number & Comparable<V>> NumericConstantExpression<V> of(V value) {
        return NumericConstantExpression.create(Type.NumericConstant, value);
    }

    static StringConstantExpression of(String value) {
        return StringConstantExpression.create(Type.StringConstant, value);
    }
}
