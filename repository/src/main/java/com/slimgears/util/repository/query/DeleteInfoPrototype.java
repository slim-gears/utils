package com.slimgears.util.repository.query;

import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClassWithKey;

@AutoValuePrototype
public interface DeleteInfoPrototype<
        K,
        S extends HasMetaClassWithKey<K, S, B>,
        B extends BuilderPrototype<S, B>>

        extends
        HasEntityMeta<K, S, B>,
        HasPredicate<S>,
        HasPagination {
}
