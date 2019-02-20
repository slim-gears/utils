package com.slimgears.util.repository.query;

import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClassWithKey;
import io.reactivex.Completable;

public interface EntityDeleteQuery<K, S extends HasMetaClassWithKey<K, S, B>, B extends BuilderPrototype<S, B>>
        extends QueryBuilder<EntityDeleteQuery<K, S, B>, K, S, B> {
    Completable execute();
}
