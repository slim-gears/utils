package com.slimgears.util.autovalue.queries;

import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClassWithKey;
import com.slimgears.util.autovalue.annotations.MetaClassWithKey;

public interface HasEntityMeta<K, S extends HasMetaClassWithKey<K, S, B>, B extends BuilderPrototype<S, B>> {
    MetaClassWithKey<K, S, B> metaClass();
}
