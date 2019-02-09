package com.slimgears.util.autovalue.expressions;

public interface UnaryOperationExpression<V, R> extends ValueExpression<R> {
    ValueExpression<V> operand();
}
