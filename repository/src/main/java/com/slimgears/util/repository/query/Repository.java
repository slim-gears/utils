package com.slimgears.util.repository.query;

import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClassWithKey;
import com.slimgears.util.autovalue.annotations.MetaClassWithKey;

public interface Repository {
    <K, S extends HasMetaClassWithKey<K, S, B>, B extends BuilderPrototype<S, B>> EntitySet<K, S, B> entities(MetaClassWithKey<K, S, B> meta);
}
