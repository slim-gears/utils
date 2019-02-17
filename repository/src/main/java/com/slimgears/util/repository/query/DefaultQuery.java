package com.slimgears.util.repository.query;

import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClassWithKey;
import com.slimgears.util.autovalue.annotations.MetaClassWithKey;
import com.slimgears.util.repository.expressions.Aggregator;
import com.slimgears.util.repository.expressions.CollectionExpression;
import com.slimgears.util.repository.expressions.ObjectExpression;
import com.slimgears.util.repository.expressions.PropertyExpression;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

public class DefaultQuery<K, S extends HasMetaClassWithKey<K, S, B>, B extends BuilderPrototype<S, B>, T> implements Query<S, T> {
    private final QueryProvider queryProvider;
    private final MetaClassWithKey<K, S, B> metaClass;
    private final CollectionExpression<Collection<S>, T> expression;

    public static <K, S extends HasMetaClassWithKey<K, S, B>, B extends BuilderPrototype<S, B>> Query<S, S> create(QueryProvider queryProvider, MetaClassWithKey<K, S, B> metaClass) {
        return new DefaultQuery<>(queryProvider, metaClass, CollectionExpression.arg());
    }

    protected DefaultQuery(QueryProvider queryProvider, MetaClassWithKey<K, S, B> metaClass, CollectionExpression<Collection<S>, T> expression) {
        this.queryProvider = queryProvider;
        this.metaClass = metaClass;
        this.expression = expression;
    }

    protected <R> DefaultQuery<K, S, B, R> create(QueryProvider queryProvider, MetaClassWithKey<K, S, B> sourceMeta, CollectionExpression<Collection<S>, R> expression) {
        return new DefaultQuery<>(queryProvider, sourceMeta, expression);
    }

    @Override
    public <R> Query<S, R> apply(Function<CollectionExpression<Collection<S>, T>, CollectionExpression<Collection<S>, R>> config) {
        return create(queryProvider, metaClass, config.apply(expression));
    }

    @Override
    public Retrieve<S, T> retrieve() {
        return new Retrieve<S, T>() {
            private final QueryInfo.Builder<K, S, T, B> builder = QueryInfo
                    .<K, S, T, B>builder()
                    .metaClass(metaClass)
                    .expression(expression);

            @Override
            public Retrieve<S, T> include(PropertyExpression<S, T, ?, ?>... propertyExpressions) {
                Arrays
                        .stream(propertyExpressions)
                        .map(PropertyExpression::property)
                        .forEach(p -> builder.propertiesBuilder().add(p));
                return this;
            }

            @Override
            public <R> Single<R> aggregate(Aggregator<S, T, R, ?> aggregator) {
                return queryProvider.aggregate(builder.build(), aggregator);
            }

            @Override
            public Observable<T> execute() {
                return queryProvider.query(builder.build());
            }

            @Override
            public Retrieve<S, T> limit(long limit) {
                builder.limit(limit);
                return this;
            }

            @Override
            public Retrieve<S, T> skip(long skip) {
                builder.skip(skip);
                return this;
            }
        };
    }

    @Override
    public Live<S, T> live() {
        return new Live<S, T>() {
            private final QueryInfo.Builder<K, S, T, B> builder = QueryInfo
                    .<K, S, T, B>builder()
                    .metaClass(metaClass)
                    .expression(expression);

            @Override
            public Observable<Long> count() {
                return queryProvider.liveAggregate(builder.build(), Aggregator.count());
            }

            @Override
            public Observable<Notification<T>> execute() {
                return queryProvider.liveQuery(builder.build());
            }

            @Override
            public Live<S, T> limit(long limit) {
                builder.limit(limit);
                return this;
            }

            @Override
            public Live<S, T> skip(long skip) {
                builder.skip(skip);
                return this;
            }
        };
    }

    @Override
    public Completable delete() {
        return queryProvider.delete(DeleteInfo.<K, S, T, B>builder().expression(expression).build());
    }

    @Override
    public Update<S, T> testAndUpdate(ObjectExpression<T, Boolean> predicate) {
        return new Update<S, T>() {
            private final UpdateInfo.Builder<K, S, T, B> builder = UpdateInfo
                    .<K, S, T, B>builder()
                    .metaClass(metaClass)
                    .predicate(predicate);

            @Override
            public <V> Update<S, T> property(PropertyExpression<S, T, ?, V> property, ObjectExpression<T, V> update) {
                builder.propertyUpdatesAdd(PropertyUpdateInfo.create(property.property(), update));
                return this;
            }

            @Override
            public Completable execute() {
                return queryProvider.update(builder.build());
            }

            @Override
            public Update<S, T> limit(long limit) {
                builder.limit(limit);
                return this;
            }

            @Override
            public Update<S, T> skip(long skip) {
                builder.skip(skip);
                return this;
            }
        };
    }

    @Override
    public Update<S, T> update() {
        return testAndUpdate(null);
    }
}
