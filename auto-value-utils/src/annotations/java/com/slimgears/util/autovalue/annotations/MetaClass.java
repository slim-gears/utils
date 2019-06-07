package com.slimgears.util.autovalue.annotations;

import com.slimgears.util.reflect.TypeToken;

public interface MetaClass<T> {
    TypeToken<? extends MetaBuilder<T>> builderClass();
    TypeToken<T> objectClass();
    Iterable<PropertyMeta<T, ?>> properties();
    <V> PropertyMeta<T, V> getProperty(String propertyName);
    <B extends MetaBuilder<T>> B createBuilder();

    default String simpleName() {
        return asClass().getSimpleName();
    }

    default Class<T> asClass() {
        return objectClass().asClass();
    }
}
