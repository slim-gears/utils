package com.slimgears.util.autovalue.expressions;

import com.slimgears.util.autovalue.expressions.internal.BooleanBinaryOperationExpression;
import com.slimgears.util.autovalue.expressions.internal.NumericUnaryOperationExpression;
import com.slimgears.util.autovalue.expressions.internal.StringBinaryOperationExpression;
import com.slimgears.util.autovalue.expressions.internal.StringUnaryOperationExpression;

public interface StringExpression extends ComparableExpression<String> {
    default BooleanExpression contains(ObjectExpression<String> substr) {
        return BooleanBinaryOperationExpression.create(Type.Contains, this, substr);
    }

    default BooleanExpression contains(String substr) {
        return contains(ConstantExpression.of(substr));
    }

    default BooleanExpression startsWith(ObjectExpression<String> substr) {
        return BooleanBinaryOperationExpression.create(Type.StartsWith, this, substr);
    }

    default BooleanExpression startsWith(String substr) {
        return startsWith(ConstantExpression.of(substr));
    }

    default BooleanExpression endsWith(ObjectExpression<String> substr) {
        return BooleanBinaryOperationExpression.create(Type.EndsWith, this, substr);
    }

    default BooleanExpression endsWith(String substr) {
        return endsWith(ConstantExpression.of(substr));
    }

    default BooleanExpression matches(String substr) {
        return matches(ConstantExpression.of(substr));
    }

    default NumericExpression<Integer> length() {
        return NumericUnaryOperationExpression.create(Type.Length, this);
    }

    default StringExpression concat(ObjectExpression<String> suffix) {
        return StringBinaryOperationExpression.create(Type.Concat, this, suffix);
    }

    default StringExpression concat(String suffix) {
        return concat(ConstantExpression.of(suffix));
    }

    default StringExpression toLower() {
        return StringUnaryOperationExpression.create(Type.ToLower, this);
    }

    default StringExpression toUpper() {
        return StringUnaryOperationExpression.create(Type.ToUpper, this);
    }

    default StringExpression trim() {
        return StringUnaryOperationExpression.create(Type.Trim, this);
    }
}
