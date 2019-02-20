package com.slimgears.util.repository.query;

import com.slimgears.util.repository.expressions.Aggregator;
import com.slimgears.util.repository.expressions.UnaryOperationExpression;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;

public interface SelectQuery<S, T> {
    Maybe<T> first();
    <R, E extends UnaryOperationExpression<S, Collection<T>, R>> Single<R> aggregate(Aggregator<S, T, R, E> aggregator);
    Observable<T> retrieve();

    default Single<Long> count() {
        return aggregate(Aggregator.count());
    }
}
