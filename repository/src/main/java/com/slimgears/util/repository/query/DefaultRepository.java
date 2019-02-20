package com.slimgears.util.repository.query;

import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClassWithKey;
import com.slimgears.util.autovalue.annotations.MetaClassWithKey;

import java.util.HashMap;
import java.util.Map;

public class DefaultRepository implements Repository {
    private final QueryProvider queryProvider;
    private final Map<MetaClassWithKey, EntitySet> entitySetMap = new HashMap<>();

    public DefaultRepository(QueryProvider queryProvider) {
        this.queryProvider = queryProvider;
    }

    private <K, S extends HasMetaClassWithKey<K, S, B>, B extends BuilderPrototype<S, B>> EntitySet<K, S, B> createEntitySet(MetaClassWithKey<K, S, B> metaClass) {
        return DefaultEntitySet.create(queryProvider, metaClass);
    }

    @Override
    public <K, S extends HasMetaClassWithKey<K, S, B>, B extends BuilderPrototype<S, B>> EntitySet<K, S, B> entities(MetaClassWithKey<K, S, B> meta) {
        //noinspection unchecked
        return (EntitySet<K, S, B>)entitySetMap.computeIfAbsent(meta, this::createEntitySet);
    }
}
