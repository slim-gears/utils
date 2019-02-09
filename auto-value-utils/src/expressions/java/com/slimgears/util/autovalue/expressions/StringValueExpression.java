package com.slimgears.util.autovalue.expressions;

public interface StringValueExpression extends ComparableValueExpression<String> {
    default BooleanExpression contains(ValueExpression<String> substr) {
        return BooleanBinaryOperationExpression.create("containsStr", this, substr);
    }

    default BooleanExpression contains(String substr) {
        return contains(ConstantExpression.of(substr));
    }

    default BooleanExpression startsWith(ValueExpression<String> substr) {
        return BooleanBinaryOperationExpression.create("startsWith", this, substr);
    }

    default BooleanExpression startsWith(String substr) {
        return startsWith(ConstantExpression.of(substr));
    }

    default BooleanExpression endsWith(ValueExpression<String> substr) {
        return BooleanBinaryOperationExpression.create("endsWith", this, substr);
    }

    default BooleanExpression endsWith(String substr) {
        return endsWith(ConstantExpression.of(substr));
    }

    default BooleanExpression matches(ValueExpression<String> substr) {
        return BooleanBinaryOperationExpression.create("matches", this, substr);
    }

    default BooleanExpression matches(String substr) {
        return matches(ConstantExpression.of(substr));
    }
}
