package com.slimgears.util.repository.query;

import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClassWithKey;
import com.slimgears.util.autovalue.annotations.MetaClassWithKey;

import java.util.HashMap;
import java.util.Map;

public class DefaultRepository implements Repository {
    private final QueryProvider queryProvider;
    private final Map<MetaClassWithKey, Query> queryMap = new HashMap<>();

    public DefaultRepository(QueryProvider queryProvider) {
        this.queryProvider = queryProvider;
    }

    @Override
    public <K, S extends HasMetaClassWithKey<K, S, B>, B extends BuilderPrototype<S, B>> Query<S, S> collection(MetaClassWithKey<K, S, B> metaClass) {
        //noinspection unchecked
        return (Query<S, S>)queryMap.computeIfAbsent(metaClass, this::createQuery);
    }

    private <K, S extends HasMetaClassWithKey<K, S, B>, B extends BuilderPrototype<S, B>> Query<S, S> createQuery(MetaClassWithKey<K, S, B> metaClass) {
        return DefaultQuery.create(queryProvider, metaClass);
    }
}
