package com.slimgears.util.repository.query;

import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClassWithKey;
import com.slimgears.util.autovalue.annotations.MetaClassWithKey;
import com.slimgears.util.repository.expressions.Aggregator;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

public interface QueryProvider {
    <K, S extends HasMetaClassWithKey<K, S, B>, T, B extends BuilderPrototype<S, B>> Single<T> find(
            MetaClassWithKey<K, S, B> metaClass,
            K id);

    <K, S extends HasMetaClassWithKey<K, S, B>, B extends BuilderPrototype<S, B>> Single<List<S>> update(Iterable<S> entity);

    <K, S extends HasMetaClassWithKey<K, S, B>, T, B extends BuilderPrototype<S, B>, Q extends
            HasEntityMeta<K, S, B>
            & HasPredicate<S>
            & HasMapping<S, T>
            & HasPagination
            & HasSortingInfo<S>
            & HasProperties<T>> Observable<T> query(Q query);

    <K, S extends HasMetaClassWithKey<K, S, B>, T, B extends BuilderPrototype<S, B>, Q extends
            HasEntityMeta<K, S, B>
            & HasPredicate<S>
            & HasMapping<S, T>
            & HasPagination
            & HasSortingInfo<S>
            & HasProperties<T>> Observable<Notification<T>> liveQuery(Q query);

    <K, S extends HasMetaClassWithKey<K, S, B>, T, R, B extends BuilderPrototype<S, B>, Q extends
            HasEntityMeta<K, S, B>
            & HasPredicate<S>
            & HasMapping<S, T>
            & HasPagination
            & HasProperties<T>> Single<R> aggregate(Q query, Aggregator<S, T, R, ?> aggregator);

    <K, S extends HasMetaClassWithKey<K, S, B>, T, R, B extends BuilderPrototype<S, B>, Q extends
            HasEntityMeta<K, S, B>
            & HasPredicate<S>
            & HasMapping<S, T>
            & HasPagination
            & HasProperties<T>> Observable<R> liveAggregate(Q query, Aggregator<S, T, R, ?> aggregator);

    <K, S extends HasMetaClassWithKey<K, S, B>, B extends BuilderPrototype<S, B>, Q extends
            HasEntityMeta<K, S, B>
            & HasPredicate<S>
            & HasPropertyUpdates<S>> Observable<S> update(Q update);

    <K, S extends HasMetaClassWithKey<K, S, B>, B extends BuilderPrototype<S, B>, Q extends
            HasEntityMeta<K, S, B>
            & HasPredicate<S>> Completable delete(Q delete);
}
