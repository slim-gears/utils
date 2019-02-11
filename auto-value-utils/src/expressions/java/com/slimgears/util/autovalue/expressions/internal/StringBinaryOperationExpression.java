package com.slimgears.util.autovalue.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.expressions.BinaryOperationExpression;
import com.slimgears.util.autovalue.expressions.ObjectExpression;
import com.slimgears.util.autovalue.expressions.StringExpression;

@AutoValue
public abstract class StringBinaryOperationExpression<T1, T2> implements BinaryOperationExpression<T1, T2, String>, StringExpression {
    @JsonCreator
    public static <T1, T2> StringBinaryOperationExpression<T1, T2> create(
            @JsonProperty("type") Type type,
            @JsonProperty("left") ObjectExpression<T1> left,
            @JsonProperty("right") ObjectExpression<T2> right) {
        return new AutoValue_StringBinaryOperationExpression<>(type, left, right);
    }
}
