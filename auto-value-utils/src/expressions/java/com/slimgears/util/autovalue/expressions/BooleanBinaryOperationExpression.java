package com.slimgears.util.autovalue.expressions;

public interface BooleanBinaryOperationExpression<V1, V2> extends BinaryOperationExpression<V1, V2, Boolean>, BooleanExpression {
    static <V1, V2> BooleanBinaryOperationExpression<V1, V2> create(String type, ValueExpression<V1> left, ValueExpression<V2> right) {
        return new BooleanBinaryOperationExpression<V1, V2>() {
            @Override
            public ValueExpression<V1> left() {
                return left;
            }

            @Override
            public ValueExpression<V2> right() {
                return right;
            }

            @Override
            public String type() {
                return type;
            }
        };
    }
}
