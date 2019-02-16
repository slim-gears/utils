package com.slimgears.util.autovalue.queries;

import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClassWithKey;

@AutoValuePrototype
public interface QueryInfoPrototype<K, S extends HasMetaClassWithKey<K, S, B>, T, B extends BuilderPrototype<S, B>> extends
        HasEntityMeta<K, S, B>,
        HasExpression<S, T>,
        HasProperties<T>,
        HasPagination {
}
