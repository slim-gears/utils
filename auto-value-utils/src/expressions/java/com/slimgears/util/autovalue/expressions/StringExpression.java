package com.slimgears.util.autovalue.expressions;

public interface StringExpression extends ComparableExpression<String> {
    default BooleanExpression contains(Expression<String> substr) {
        return BooleanBinaryOperationExpression.create(ExpressionType.Contains, this, substr);
    }

    default BooleanExpression contains(String substr) {
        return contains(ConstantExpression.of(substr));
    }

    default BooleanExpression startsWith(Expression<String> substr) {
        return BooleanBinaryOperationExpression.create(ExpressionType.StartsWith, this, substr);
    }

    default BooleanExpression startsWith(String substr) {
        return startsWith(ConstantExpression.of(substr));
    }

    default BooleanExpression endsWith(Expression<String> substr) {
        return BooleanBinaryOperationExpression.create(ExpressionType.EndsWith, this, substr);
    }

    default BooleanExpression endsWith(String substr) {
        return endsWith(ConstantExpression.of(substr));
    }

    default BooleanExpression matches(Expression<String> substr) {
        return BooleanBinaryOperationExpression.create(ExpressionType.Matches, this, substr);
    }

    default BooleanExpression matches(String substr) {
        return matches(ConstantExpression.of(substr));
    }

    default NumericExpression<Integer> length() {
        return NumericUnaryOperationExpression.create(ExpressionType.Length, this);
    }
}
