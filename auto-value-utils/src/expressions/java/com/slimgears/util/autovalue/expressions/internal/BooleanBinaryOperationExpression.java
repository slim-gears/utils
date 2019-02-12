package com.slimgears.util.autovalue.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.expressions.BinaryOperationExpression;
import com.slimgears.util.autovalue.expressions.BooleanExpression;
import com.slimgears.util.autovalue.expressions.ObjectExpression;

@AutoValue
public abstract class BooleanBinaryOperationExpression<S, V1, V2> implements BinaryOperationExpression<S, V1, V2, Boolean>, BooleanExpression<S> {
    @JsonCreator
    public static <S, V1, V2> BooleanBinaryOperationExpression<S, V1, V2> create(
            @JsonProperty("type") Type type,
            @JsonProperty("left") ObjectExpression<S, V1> left,
            @JsonProperty("right") ObjectExpression<S, V2> right) {
        return new AutoValue_BooleanBinaryOperationExpression<>(type, left, right);
    }
}
