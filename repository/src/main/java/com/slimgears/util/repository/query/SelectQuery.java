package com.slimgears.util.repository.query;

import com.slimgears.util.repository.expressions.Aggregator;
import com.slimgears.util.repository.expressions.UnaryOperationExpression;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.IntFunction;

import java.util.Collection;
import java.util.List;

public interface SelectQuery<S, T> {
    Maybe<T> first();
    Single<List<T>> toList();
    Single<T[]> toArray(IntFunction<T[]> arrayCreator);
    Single<Long> count();
    <R, E extends UnaryOperationExpression<S, Collection<T>, R>> Single<R> aggregate(Aggregator<S, T, R, E> aggregator);
    Observable<T> retrieve();
}
