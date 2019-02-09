package com.slimgears.util.autovalue.expressions;

public interface BooleanExpression extends ComparableValueExpression<Boolean> {
    default BooleanExpression and(BooleanExpression value) {
        return BooleanBinaryOperationExpression.create("and", this, value);
    }

    default BooleanExpression or(BooleanExpression value) {
        return BooleanBinaryOperationExpression.create("or", this, value);
    }

    default BooleanExpression not() {
        return BooleanUnaryOperationExpression.create("not", this);
    }
}
