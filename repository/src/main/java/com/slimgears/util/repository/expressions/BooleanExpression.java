package com.slimgears.util.repository.expressions;

import com.slimgears.util.repository.expressions.internal.BooleanBinaryOperationExpression;
import com.slimgears.util.repository.expressions.internal.BooleanConstantExpression;
import com.slimgears.util.repository.expressions.internal.BooleanUnaryOperationExpression;

import java.util.Arrays;

public interface BooleanExpression<S> extends ComparableExpression<S, Boolean> {
    default BooleanExpression<S> and(ObjectExpression<S, Boolean> value) {
        return BooleanBinaryOperationExpression.create(Type.And, this, value);
    }

    default BooleanExpression<S> or(ObjectExpression<S, Boolean> value) {
        return BooleanBinaryOperationExpression.create(Type.Or, this, value);
    }

    default BooleanExpression<S> not() {
        return BooleanUnaryOperationExpression.create(Type.Not, this);
    }

    static <S> BooleanExpression<S> ofTrue() {
        return BooleanConstantExpression.create(Type.BooleanConstant, true);
    }

    static <S> BooleanExpression<S> ofFalse() {
        return BooleanConstantExpression.create(Type.BooleanConstant, false);
    }

    @SafeVarargs
    static <S> BooleanExpression<S> and(BooleanExpression<S> first, BooleanExpression<S>... expressions) {
        return Arrays.stream(expressions).reduce(first, (a, b) -> a.and(b), (a, b) -> a.and(b));
    }

    @SafeVarargs
    static <S> BooleanExpression<S> or(BooleanExpression<S> first, BooleanExpression<S>... expressions) {
        return Arrays.stream(expressions).reduce(first, (a, b) -> a.or(b), (a, b) -> a.or(b));
    }
}
