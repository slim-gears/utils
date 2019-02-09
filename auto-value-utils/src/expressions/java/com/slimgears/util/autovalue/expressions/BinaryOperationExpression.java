package com.slimgears.util.autovalue.expressions;

public interface BinaryOperationExpression<T1, T2, R> extends ValueExpression<R> {
    ValueExpression<T1> left();
    ValueExpression<T2> right();
}
