package com.slimgears.util.repository.query;

import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClassWithKey;
import com.slimgears.util.repository.expressions.ObjectExpression;

public interface QueryBuilder<__B extends QueryBuilder<__B, K, S, B>, K, S extends HasMetaClassWithKey<K, S, B>, B extends BuilderPrototype<S, B>> {
    __B where(ObjectExpression<S, Boolean> predicate);
    __B limit(long limit);
    __B skip(long skip);
}
