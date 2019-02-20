package com.slimgears.util.repository.query;

import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.HasMetaClassWithKey;
import com.slimgears.util.repository.expressions.ConstantExpression;
import com.slimgears.util.repository.expressions.ObjectExpression;
import com.slimgears.util.repository.expressions.PropertyExpression;
import io.reactivex.Observable;

public interface EntityUpdateQuery<K, S extends HasMetaClassWithKey<K, S, B>, B extends BuilderPrototype<S, B>>
            extends QueryBuilder<EntityUpdateQuery<K, S, B>, K, S, B> {
        <T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>, V> EntityUpdateQuery<K, S, B> set(PropertyExpression<S, T, TB, V> property, ObjectExpression<S, V> value);

    default <T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>, V> EntityUpdateQuery<K, S, B> set(PropertyExpression<S, T, TB, V> property, V value) {
        return set(property, ConstantExpression.of(value));
    }

    Observable<S> execute();
}
