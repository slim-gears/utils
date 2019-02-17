package com.slimgears.util.repository.query;

import com.slimgears.util.repository.expressions.Aggregator;
import com.slimgears.util.repository.expressions.CollectionExpression;
import com.slimgears.util.repository.expressions.ObjectExpression;
import com.slimgears.util.repository.expressions.PropertyExpression;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.function.Function;

public interface Query<S, T> {
    interface Modification<T> {
        T oldObject();
        T newObject();
    }

    interface HasPagination<P extends HasPagination<P>> {
        P limit(long limit);
        P skip(long skip);
    }

    interface Retrieve<S, T> extends HasPagination<Retrieve<S, T>> {
        Retrieve<S, T> include(PropertyExpression<S, T, ?, ?>... propertyExpressions);
        <R> Single<R> aggregate(Aggregator<S, T, R, ?> aggregator);

        default Single<Long> count() {
            return aggregate(Aggregator.count());
        }

        Observable<T> execute();
    }

    interface Update<S, T> extends HasPagination<Update<S, T>> {
        <V> Update<S, T> property(PropertyExpression<S, T, ?, V> property, ObjectExpression<T, V> update);
        Completable execute();
    }

    interface Live<S, T> extends HasPagination<Live<S, T>> {
        Observable<Long> count();
        Observable<Notification<T>> execute();
    }

    <R> Query<S, R> apply(Function<CollectionExpression<Collection<S>, T>, CollectionExpression<Collection<S>, R>> config);

    default <R> Query<S, R> map(ObjectExpression<T, R> mapper) {
        return apply(exp -> exp.map(mapper));
    }

    default <R> Query<S, R> flatMap(ObjectExpression<T, Collection<R>> mapper) {
        return apply(exp -> exp.flatMap(mapper));
    }

    default Query<S, T> filter(ObjectExpression<T, Boolean> predicate) {
        return apply(exp -> exp.filter(predicate));
    }

    Retrieve<S, T> retrieve();
    Live<S, T> live();
    Completable delete();
    Update<S, T> testAndUpdate(ObjectExpression<T, Boolean> predicate);
    Update<S, T> update();
}
