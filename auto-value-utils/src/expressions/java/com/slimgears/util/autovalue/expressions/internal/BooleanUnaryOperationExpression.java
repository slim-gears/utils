package com.slimgears.util.autovalue.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.expressions.BooleanExpression;
import com.slimgears.util.autovalue.expressions.ObjectExpression;
import com.slimgears.util.autovalue.expressions.UnaryOperationExpression;

@AutoValue
public abstract class BooleanUnaryOperationExpression<V> implements UnaryOperationExpression<V, Boolean>, BooleanExpression {
    @JsonCreator
    public static <V> BooleanUnaryOperationExpression<V> create(
            @JsonProperty("type") Type type,
            @JsonProperty("operand") ObjectExpression<V> operand) {
        return new AutoValue_BooleanUnaryOperationExpression<>(type, operand);
    }
}
