package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface StringConstantExpression extends ComparableConstantExpression<String>, StringExpression {
    @JsonCreator
    static StringConstantExpression of(@JsonProperty("value") String value) {
        return new StringConstantExpression() {
            @Override
            public String value() {
                return value;
            }

            @Override
            public ExpressionType type() {
                return ExpressionType.StringConstant;
            }
        };
    }
}
