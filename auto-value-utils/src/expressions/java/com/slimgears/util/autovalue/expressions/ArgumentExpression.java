package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.reflect.TypeToken;

public interface ArgumentExpression<T> extends Expression<T> {
    TypeToken<T> argumentType();

    default <B, V> PropertyExpression<T, B, V> property(PropertyMeta<T, B, V> property) {
        return PropertyExpression.of(this, property);
    }

    default <B, V extends Comparable<V>> ComparablePropertyExpression<T, B, V> comparableProperty(PropertyMeta<T, B, V> property) {
        return ComparablePropertyExpression.of(this, property);
    }

    default <B, V extends Number & Comparable<V>> NumericPropertyExpression<T, B, V> numericProperty(PropertyMeta<T, B, V> property) {
        return NumericPropertyExpression.of(this, property);
    }

    default <B> StringPropertyExpression<T, B> stringProperty(PropertyMeta<T, B, String> property) {
        return StringPropertyExpression.of(this, property);
    }

    @JsonCreator
    static <T> ArgumentExpression<T> of(@JsonProperty("argumentType") TypeToken<T> argumentType) {
        return new ArgumentExpression<T>() {
            @Override
            public TypeToken<T> argumentType() {
                return argumentType;
            }

            @Override
            public ExpressionType type() {
                return ExpressionType.Argument;
            }
        };
    }
}
