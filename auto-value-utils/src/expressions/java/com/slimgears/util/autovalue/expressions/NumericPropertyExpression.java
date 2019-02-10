package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.slimgears.util.autovalue.annotations.PropertyMeta;

public interface NumericPropertyExpression<T, B, V extends Number & Comparable<V>> extends ComparablePropertyExpression<T, B, V>, NumericExpression<V> {
    @JsonCreator
    static <T, B, V extends Number & Comparable<V>> NumericPropertyExpression<T, B, V> of(
            @JsonProperty("target") Expression<T> target,
            @JsonProperty("property") PropertyMeta<T, B, V> property) {
        return new NumericPropertyExpression<T, B, V>() {
            @Override
            public Expression<T> target() {
                return target;
            }

            @Override
            public PropertyMeta<T, B, V> property() {
                return property;
            }

            @Override
            public ExpressionType type() {
                return ExpressionType.NumericProperty;
            }
        };
    }
}
