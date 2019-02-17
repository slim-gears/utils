package com.slimgears.util.repository.query;

import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClassWithKey;
import com.slimgears.util.autovalue.annotations.MetaClassWithKey;
import com.slimgears.util.repository.expressions.Aggregator;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface QueryProvider {
    <K, S extends HasMetaClassWithKey<K, S, B>, T, B extends BuilderPrototype<S, B>> Single<T> find(
            MetaClassWithKey<K, S, B> metaClass,
            K id);

    <K, S extends HasMetaClassWithKey<K, S, B>, T, B extends BuilderPrototype<S, B>, Q extends
            HasEntityMeta<K, S, B>
            & HasExpression<S, T>
            & HasPagination
            & HasProperties<T>> Observable<T> query(Q query);

    <K, S extends HasMetaClassWithKey<K, S, B>, T, B extends BuilderPrototype<S, B>, Q extends
            HasEntityMeta<K, S, B>
            & HasExpression<S, T>
            & HasPagination
            & HasProperties<T>> Observable<Notification<T>> liveQuery(Q query);

    <K, S extends HasMetaClassWithKey<K, S, B>, T, R, B extends BuilderPrototype<S, B>, Q extends
            HasEntityMeta<K, S, B>
            & HasExpression<S, T>
            & HasPagination
            & HasProperties<T>> Single<R> aggregate(Q query, Aggregator<S, T, R, ?> aggregator);

    <K, S extends HasMetaClassWithKey<K, S, B>, T, R, B extends BuilderPrototype<S, B>, Q extends
            HasEntityMeta<K, S, B>
            & HasExpression<S, T>
            & HasPagination
            & HasProperties<T>> Observable<R> liveAggregate(Q query, Aggregator<S, T, R, ?> aggregator);

    <K, S extends HasMetaClassWithKey<K, S, B>, T, B extends BuilderPrototype<S, B>, Q extends
            HasEntityMeta<K, S, B>
            & HasExpression<S, T>
            & HasPagination
            & HasPropertyUpdates<T>> Completable update(Q update);

    <K, S extends HasMetaClassWithKey<K, S, B>, T, B extends BuilderPrototype<S, B>, Q extends
            HasEntityMeta<K, S, B>
            & HasExpression<S, T>
            & HasPagination> Completable delete(Q delete);
}
