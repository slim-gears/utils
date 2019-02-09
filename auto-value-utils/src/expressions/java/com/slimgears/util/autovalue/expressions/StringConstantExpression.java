package com.slimgears.util.autovalue.expressions;

public interface StringConstantExpression extends ComparableConstantExpression<String>, StringValueExpression {
    static StringConstantExpression of(String value) {
        return new StringConstantExpression() {
            @Override
            public String value() {
                return value;
            }

            @Override
            public String type() {
                return "stringConstant";
            }
        };
    }
}
