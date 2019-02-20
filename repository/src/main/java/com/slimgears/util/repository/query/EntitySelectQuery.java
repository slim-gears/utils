package com.slimgears.util.repository.query;

import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClassWithKey;
import com.slimgears.util.repository.expressions.ObjectExpression;
import io.reactivex.Single;

import java.util.Map;

public interface EntitySelectQuery<S, K, T extends HasMetaClassWithKey<K, T, B>, B extends BuilderPrototype<T, B>>
    extends SelectQuery<S, T> {
    <TK> Single<Map<TK, T>> toMap(ObjectExpression<T, TK> selector);
    Single<Map<K, T>> toMap();
}
