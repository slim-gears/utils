package com.slimgears.util.autovalue.expressions;

public interface BooleanUnaryOperationExpression<V> extends UnaryOperationExpression<V, Boolean>, BooleanExpression {
    static <V> BooleanUnaryOperationExpression<V> create(String type, ValueExpression<V> operand) {
        return new BooleanUnaryOperationExpression<V>() {
            @Override
            public ValueExpression<V> operand() {
                return operand;
            }

            @Override
            public String type() {
                return type;
            }
        };
    }
}
