package com.slimgears.util.repository.query;

import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.HasMetaClassWithKey;
import com.slimgears.util.autovalue.annotations.MetaClassWithKey;
import com.slimgears.util.repository.expressions.ObjectExpression;
import com.slimgears.util.repository.expressions.PropertyExpression;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

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

            @Override
            public SelectQueryBuilder<K, S, B> orderBy(PropertyExpression<S, ?, ?, ?> field, boolean ascending) {
                return null;
            }

            @Override
            public EntitySelectQuery<S, K, S, B> select() {
                return null;
            }

            @Override
            public <T> SelectQuery<S, T> select(ObjectExpression<S, T> expression) {
                return null;
            }

            @Override
            public <TK, T extends HasMetaClassWithKey<TK, T, TB>, TB extends BuilderPrototype<T, TB>> EntitySelectQuery<S, TK, T, TB> select(PropertyExpression<S, ?, ?, T> expression) {
                return null;
            }

            @Override
            public EntityLiveSelectQuery<S, K, S, B> liveSelect() {
                return null;
            }

            @Override
            public <T> LiveSelectQuery<S, T> liveSelect(ObjectExpression<S, T> expression) {
                return null;
            }

            @Override
            public <TK, T extends HasMetaClassWithKey<TK, T, TB>, TB extends BuilderPrototype<T, TB>> EntityLiveSelectQuery<S, TK, T, TB> liveSelect(PropertyExpression<S, ?, ?, T> expression) {
                return null;
            }

            @Override
            public SelectQueryBuilder<K, S, B> where(ObjectExpression<S, Boolean> predicate) {
                return null;
            }

            @Override
            public SelectQueryBuilder<K, S, B> limit(long limit) {
                return null;
            }

            @Override
            public SelectQueryBuilder<K, S, B> skip(long skip) {
                return null;
            }
        };
    }

    @Override
    public Single<List<S>> update(Iterable<S> entities) {
        return null;
    }
}
