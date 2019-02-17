package com.slimgears.util.repository.query;

import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClassWithKey;

@AutoValuePrototype
public interface UpdateInfoPrototype<K, S extends HasMetaClassWithKey<K, S, B>, T, B extends BuilderPrototype<S, B>> extends
        HasExpression<S, T>,
        HasEntityMeta<K, S, B>,
        HasPredicate<T>,
        HasPropertyUpdates<T>,
        HasPagination {
}
