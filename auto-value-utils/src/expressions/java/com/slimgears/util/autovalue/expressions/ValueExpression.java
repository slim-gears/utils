package com.slimgears.util.autovalue.expressions;

import java.util.Collection;

public interface ValueExpression<V> {
    String type();

    default BooleanExpression eq(ValueExpression<V> value) {
        return BooleanBinaryOperationExpression.create("equals", this, value);
    }

    default BooleanExpression eq(V value) {
        return eq(ConstantExpression.of(value));
    }

    default BooleanExpression notEq(ValueExpression<V> value) {
        return eq(value).not();
    }

    default BooleanExpression notEq(V value) {
        return eq(value).not();
    }

    default BooleanExpression isNull() {
        return BooleanUnaryOperationExpression.create("isNull", this);
    }

    default BooleanExpression isNotNull() {
        return isNull().not();
    }

    default BooleanExpression inArray(ValueExpression<V[]> values) {
        return BooleanBinaryOperationExpression.create("inArray", this, values);
    }

    default BooleanExpression inArray(V... values) {
        return inArray(ConstantExpression.of(values));
    }

    default BooleanExpression in(ValueExpression<Collection<V>> values) {
        return BooleanBinaryOperationExpression.create("in", this, values);
    }

    default BooleanExpression in(Collection<V> values) {
        return in(ConstantExpression.of(values));
    }
}
