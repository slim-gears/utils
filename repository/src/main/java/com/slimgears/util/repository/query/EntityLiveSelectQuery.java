package com.slimgears.util.repository.query;

import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClassWithKey;
import com.slimgears.util.repository.expressions.ObjectExpression;
import io.reactivex.Observable;

import java.util.Map;

public interface EntityLiveSelectQuery<S, K, T extends HasMetaClassWithKey<K, T, B>, B extends BuilderPrototype<T, B>>
    extends LiveSelectQuery<S, T> {
    <TK> Observable<Map<TK, T>> toMap(ObjectExpression<T, TK> selector);
    Observable<Map<K, T>> toMap();
}
