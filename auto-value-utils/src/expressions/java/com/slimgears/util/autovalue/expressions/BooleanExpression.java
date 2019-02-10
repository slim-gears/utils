package com.slimgears.util.autovalue.expressions;

public interface BooleanExpression extends ComparableExpression<Boolean> {
    default BooleanExpression and(BooleanExpression value) {
        return BooleanBinaryOperationExpression.create(ExpressionType.And, this, value);
    }

    default BooleanExpression or(BooleanExpression value) {
        return BooleanBinaryOperationExpression.create(ExpressionType.Or, this, value);
    }

    default BooleanExpression not() {
        return BooleanUnaryOperationExpression.create(ExpressionType.Not, this);
    }
}
