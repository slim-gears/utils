package com.slimgears.util.autovalue.expressions;

import com.slimgears.util.autovalue.expressions.internal.BooleanBinaryOperationExpression;
import com.slimgears.util.autovalue.expressions.internal.BooleanUnaryOperationExpression;
import com.slimgears.util.autovalue.expressions.internal.CollectionOperationExpression;

import java.util.Collection;

public interface CollectionExpression<S, E> extends ObjectExpression<S, Collection<E>> {
    default BooleanExpression<S> contains(ObjectExpression<S, E> item) {
        return BooleanBinaryOperationExpression.create(Expression.Type.Contains, this, item);
    }

    default BooleanExpression<S> contains(E item) {
        return contains(ConstantExpression.of(item));
    }

    default BooleanExpression<S> isEmpty() {
        return BooleanUnaryOperationExpression.create(Type.IsEmpty, this);
    }

    default BooleanExpression<S> isNotEmpty() {
        return isEmpty().not();
    }

    default <R> CollectionExpression<S, R> map(ObjectExpression<E, R> mapper) {
        return CollectionOperationExpression
                .create(Type.MapCollection, this, mapper);
    }

    default <R> CollectionExpression<S, R> flatMap(ObjectExpression<E, Collection<R>> mapper) {
        return CollectionOperationExpression
                .create(Type.FlatMapCollection, this, mapper);
    }

    default CollectionExpression<S, E> filter(ObjectExpression<E, Boolean> filter) {
        return CollectionOperationExpression
                .create(Type.FilterCollection, this, filter);
    }

    default BooleanExpression<S> any(ObjectExpression<E, Boolean> condition) {
        return map(condition)
                .filter(ObjectExpression.<Boolean, Boolean>arg().eq(true))
                .isNotEmpty();
    }

    default BooleanExpression<S> all(ObjectExpression<E, Boolean> condition) {
        return map(condition)
                .filter(ObjectExpression.<Boolean, Boolean>arg().eq(false))
                .isEmpty();
    }

    default <R, OE extends UnaryOperationExpression<S, Collection<E>, R>> OE aggregate(Aggregator<S, E, R, OE> aggregator) {
        return aggregator.apply(this);
    }
}
