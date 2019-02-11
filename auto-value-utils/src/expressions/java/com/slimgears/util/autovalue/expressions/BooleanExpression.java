package com.slimgears.util.autovalue.expressions;

import com.slimgears.util.autovalue.expressions.internal.BooleanBinaryOperationExpression;
import com.slimgears.util.autovalue.expressions.internal.BooleanUnaryOperationExpression;

public interface BooleanExpression extends ComparableExpression<Boolean> {
    default BooleanExpression and(BooleanExpression value) {
        return BooleanBinaryOperationExpression.create(Type.And, this, value);
    }

    default BooleanExpression or(BooleanExpression value) {
        return BooleanBinaryOperationExpression.create(Type.Or, this, value);
    }

    default BooleanExpression not() {
        return BooleanUnaryOperationExpression.create(Type.Not, this);
    }
}
