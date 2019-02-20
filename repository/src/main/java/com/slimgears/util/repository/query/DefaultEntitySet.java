package com.slimgears.util.repository.query;

import com.google.common.collect.ImmutableList;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.HasMetaClassWithKey;
import com.slimgears.util.autovalue.annotations.MetaClassWithKey;
import com.slimgears.util.repository.expressions.Aggregator;
import com.slimgears.util.repository.expressions.BooleanExpression;
import com.slimgears.util.repository.expressions.ObjectExpression;
import com.slimgears.util.repository.expressions.PropertyExpression;
import com.slimgears.util.repository.expressions.UnaryOperationExpression;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultEntitySet<K, S extends HasMetaClassWithKey<K, S, B>, B extends BuilderPrototype<S, B>> implements EntitySet<K, S, B> {
    private final QueryProvider queryProvider;
    private final MetaClassWithKey<K, S, B> metaClass;

    private DefaultEntitySet(QueryProvider queryProvider, MetaClassWithKey<K, S, B> metaClass) {
        this.queryProvider = queryProvider;
        this.metaClass = metaClass;
    }

    static <K, S extends HasMetaClassWithKey<K, S, B>, B extends BuilderPrototype<S, B>> DefaultEntitySet<K, S, B> create(QueryProvider queryProvider, MetaClassWithKey<K, S, B> metaClass) {
        return new DefaultEntitySet<>(queryProvider, metaClass);
    }

    @Override
    public MetaClassWithKey<K, S, B> metaClass() {
        return metaClass;
    }

    @Override
    public EntityDeleteQuery<K, S, B> delete() {
        return new EntityDeleteQuery<K, S, B>() {
            private final DeleteInfo.Builder<K, S, B> builder = DeleteInfo.builder();

            @Override
            public Completable execute() {
                return queryProvider.delete(builder.build());
            }

            @Override
            public EntityDeleteQuery<K, S, B> where(ObjectExpression<S, Boolean> predicate) {
                builder.predicate(predicate);
                return this;
            }

            @Override
            public EntityDeleteQuery<K, S, B> limit(long limit) {
                builder.limit(limit);
                return this;
            }

            @Override
            public EntityDeleteQuery<K, S, B> skip(long skip) {
                builder.skip(skip);
                return this;
            }
        };
    }

    @Override
    public EntityUpdateQuery<K, S, B> update() {
        return new EntityUpdateQuery<K, S, B>() {
            private final UpdateInfo.Builder<K, S, B> builder = UpdateInfo.builder();

            @Override
            public <T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>, V> EntityUpdateQuery<K, S, B> set(PropertyExpression<S, T, TB, V> property, ObjectExpression<S, V> value) {
                builder.propertyUpdatesBuilder().add(PropertyUpdateInfo.create(property, value));
                return this;
            }

            @Override
            public Observable<S> execute() {
                return queryProvider.update(builder.build());
            }

            @Override
            public EntityUpdateQuery<K, S, B> where(ObjectExpression<S, Boolean> predicate) {
                builder.predicate(predicate);
                return this;
            }

            @Override
            public EntityUpdateQuery<K, S, B> limit(long limit) {
                builder.limit(limit);
                return this;
            }

            @Override
            public EntityUpdateQuery<K, S, B> skip(long skip) {
                builder.skip(skip);
                return this;
            }
        };
    }

    @Override
    public SelectQueryBuilder<K, S, B> query() {
        return new SelectQueryBuilder<K, S, B>() {
            private final ImmutableList.Builder<SortingInfo<S, ?>> sortingInfos = ImmutableList.builder();
            private final AtomicReference<BooleanExpression<S>> predicate = new AtomicReference<>(BooleanExpression.ofTrue());
            private Long limit;
            private Long skip;

            @Override
            public SelectQueryBuilder<K, S, B> orderBy(PropertyExpression<S, ?, ?, ?> field, boolean ascending) {
                sortingInfos.add(SortingInfo.create(field, ascending));
                return this;
            }

            @Override
            public <T> SelectQuery<S, T> select(ObjectExpression<S, T> expression) {
                return new SelectQuery<S, T>() {
                    private final QueryInfo.Builder<K, S, T, B> builder = QueryInfo.<K, S, T, B>builder()
                            .metaClass(metaClass)
                            .predicate(predicate.get())
                            .limit(limit)
                            .skip(skip)
                            .sorting(sortingInfos.build())
                            .mapping(expression);

                    @Override
                    public Maybe<T> first() {
                        return queryProvider.query(builder.limit(1L).build()).singleElement();
                    }

                    @Override
                    public <R, E extends UnaryOperationExpression<S, Collection<T>, R>> Single<R> aggregate(Aggregator<S, T, R, E> aggregator) {
                        return queryProvider.aggregate(builder.build(), aggregator);
                    }

                    @Override
                    public Observable<T> retrieve() {
                        return queryProvider.query(builder.limit(1L).build());
                    }
                };
            }

            @Override
            public <T> LiveSelectQuery<S, T> liveSelect(ObjectExpression<S, T> expression) {
                return new LiveSelectQuery<S, T>() {
                    private final QueryInfo.Builder<K, S, T, B> builder = QueryInfo.<K, S, T, B>builder()
                            .metaClass(metaClass)
                            .predicate(predicate.get())
                            .mapping(expression);

                    @Override
                    public Observable<T> first() {
                        QueryInfo<K, S, T, B> query = builder.limit(1L).build();
                        return queryProvider
                                .liveQuery(query)
                                .flatMapMaybe(n -> queryProvider.query(query).singleElement());
                    }

                    @Override
                    public Observable<List<T>> toList() {
                        QueryInfo<K, S, T, B> query = builder.build();
                        return queryProvider
                                .liveQuery(query)
                                .flatMapSingle(n -> queryProvider.query(query).toList());
                    }

                    @Override
                    public <R, E extends UnaryOperationExpression<S, Collection<T>, R>> Observable<R> aggregate(Aggregator<S, T, R, E> aggregator) {
                        return queryProvider.liveAggregate(builder.build(), aggregator);
                    }

                    @Override
                    public Observable<Notification<T>> observe() {
                        return queryProvider.liveQuery(builder.build());
                    }
                };
            }

            @Override
            public SelectQueryBuilder<K, S, B> where(ObjectExpression<S, Boolean> predicate) {
                this.predicate.updateAndGet(exp -> exp.and(predicate));
                return this;
            }

            @Override
            public SelectQueryBuilder<K, S, B> limit(long limit) {
                this.limit = limit;
                return this;
            }

            @Override
            public SelectQueryBuilder<K, S, B> skip(long skip) {
                this.skip = skip;
                return this;
            }
        };
    }

    @Override
    public Single<List<S>> update(Iterable<S> entities) {
        return queryProvider.update(entities);
    }
}
