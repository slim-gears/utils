package com.slimgears.util.repository.query;

import com.slimgears.util.repository.expressions.Aggregator;
import com.slimgears.util.repository.expressions.UnaryOperationExpression;
import io.reactivex.Observable;
import io.reactivex.functions.IntFunction;

import java.util.Collection;
import java.util.List;

public interface LiveSelectQuery<S, T> {
    default Observable<Long> count() {
        return aggregate(Aggregator.count());
    }

    default Observable<T[]> toArray(IntFunction<T[]> arrayCreator) {
        return toList().map(list -> list.toArray(arrayCreator.apply(list.size())));
    }

    Observable<T> first();
    Observable<List<T>> toList();
    <R, E extends UnaryOperationExpression<S, Collection<T>, R>> Observable<R> aggregate(Aggregator<S, T, R, E> aggregator);
    Observable<Notification<T>> observe();
}
