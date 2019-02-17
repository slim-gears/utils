package com.slimgears.util.repository.expressions;

import com.slimgears.util.repository.expressions.internal.BooleanBinaryOperationExpression;
import com.slimgears.util.repository.expressions.internal.NumericUnaryOperationExpression;
import com.slimgears.util.repository.expressions.internal.StringBinaryOperationExpression;
import com.slimgears.util.repository.expressions.internal.StringUnaryOperationExpression;

public interface StringExpression<S> extends ComparableExpression<S, String> {
    default BooleanExpression<S> contains(ObjectExpression<S, String> substr) {
        return BooleanBinaryOperationExpression.create(Type.Contains, this, substr);
    }

    default BooleanExpression<S> contains(String substr) {
        return contains(ConstantExpression.of(substr));
    }

    default BooleanExpression<S> startsWith(ObjectExpression<S, String> substr) {
        return BooleanBinaryOperationExpression.create(Type.StartsWith, this, substr);
    }

    default BooleanExpression<S> startsWith(String substr) {
        return startsWith(ConstantExpression.of(substr));
    }

    default BooleanExpression<S> endsWith(ObjectExpression<S, String> substr) {
        return BooleanBinaryOperationExpression.create(Type.EndsWith, this, substr);
    }

    default BooleanExpression<S> endsWith(String substr) {
        return endsWith(ConstantExpression.of(substr));
    }

    default BooleanExpression<S> matches(String substr) {
        return matches(ConstantExpression.of(substr));
    }

    default NumericExpression<S, Integer> length() {
        return NumericUnaryOperationExpression.create(Type.Length, this);
    }

    default StringExpression<S> concat(ObjectExpression<S, String> suffix) {
        return StringBinaryOperationExpression.create(Type.Concat, this, suffix);
    }

    default StringExpression<S> concat(String suffix) {
        return concat(ConstantExpression.of(suffix));
    }

    default StringExpression<S> toLower() {
        return StringUnaryOperationExpression.create(Type.ToLower, this);
    }

    default StringExpression<S> toUpper() {
        return StringUnaryOperationExpression.create(Type.ToUpper, this);
    }

    default StringExpression<S> trim() {
        return StringUnaryOperationExpression.create(Type.Trim, this);
    }
}
