package com.slimgears.util.autovalue.expressions;

import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.autovalue.expressions.internal.ArgumentExpression;
import com.slimgears.util.autovalue.expressions.internal.BooleanBinaryOperationExpression;
import com.slimgears.util.autovalue.expressions.internal.BooleanUnaryOperationExpression;

import java.util.Collection;

public interface ObjectExpression<T> extends Expression {
    default BooleanExpression eq(ObjectExpression<T> value) {
        return BooleanBinaryOperationExpression.create(Type.Equals, this, value);
    }

    default BooleanExpression eq(T value) {
        return eq(ConstantExpression.of(value));
    }

    default BooleanExpression notEq(ObjectExpression<T> value) {
        return eq(value).not();
    }

    default BooleanExpression notEq(T value) {
        return eq(value).not();
    }

    default BooleanExpression isNull() {
        return BooleanUnaryOperationExpression.create(Type.IsNull, this);
    }

    default BooleanExpression isNotNull() {
        return isNull().not();
    }

    default BooleanExpression inArray(ObjectExpression<T[]> values) {
        return BooleanBinaryOperationExpression.create(Type.ValueInArray, this, values);
    }

    default BooleanExpression inArray(T... values) {
        return inArray(ConstantExpression.of(values));
    }

    default BooleanExpression in(ObjectExpression<Collection<T>> values) {
        return BooleanBinaryOperationExpression.create(Type.ValueIn, this, values);
    }

    default BooleanExpression in(Collection<T> values) {
        return in(ConstantExpression.of(values));
    }

    default <B, V> ObjectExpression<V> objectRef(PropertyMeta<T, B, V> property) {
        return PropertyExpression.ofObject(this, property);
    }

    default <B, V extends Number & Comparable<V>> NumericExpression<V> numericRef(PropertyMeta<T, B, V> property) {
        return PropertyExpression.ofNumeric(this, property);
    }

    default <B, V extends Comparable<V>> ComparableExpression<V> comparableRef(PropertyMeta<T, B, V> property) {
        return PropertyExpression.ofComparable(this, property);
    }

    default <B> StringExpression stringRef(PropertyMeta<T, B, String> property) {
        return PropertyExpression.ofString(this, property);
    }

    default BooleanExpression matches(ObjectExpression<String> pattern) {
        return BooleanBinaryOperationExpression.create(Type.Matches, this, pattern);
    }

    default BooleanExpression matches(String pattern) {
        return matches(ConstantExpression.of(pattern));
    }

    static <T> ObjectExpression<T> arg() {
        return arg("$");
    }

    static <T> ObjectExpression<T> arg(String name) {
        return ArgumentExpression.create(Type.Argument, name);
    }
}
