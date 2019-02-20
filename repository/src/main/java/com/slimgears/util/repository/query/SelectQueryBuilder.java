package com.slimgears.util.repository.query;

import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClassWithKey;
import com.slimgears.util.repository.expressions.ObjectExpression;
import com.slimgears.util.repository.expressions.PropertyExpression;

public interface SelectQueryBuilder<K, S extends HasMetaClassWithKey<K, S, B>, B extends BuilderPrototype<S, B>>
    extends QueryBuilder<SelectQueryBuilder<K, S, B>, K, S, B> {
    SelectQueryBuilder<K, S, B> orderBy(PropertyExpression<S, ?, ?, ?> field, boolean ascending);

    default SelectQueryBuilder<K, S, B> orderBy(PropertyExpression<S, ?, ?, ?> field) {
        return orderBy(field, true);
    }

    default SelectQuery<S, S> select() {
        return select(ObjectExpression.arg());
    }

    <T> SelectQuery<S, T> select(ObjectExpression<S, T> expression);

    default LiveSelectQuery<S, S> liveSelect() {
        return liveSelect(ObjectExpression.arg());
    }

    <T> LiveSelectQuery<S, T> liveSelect(ObjectExpression<S, T> expression);
}
