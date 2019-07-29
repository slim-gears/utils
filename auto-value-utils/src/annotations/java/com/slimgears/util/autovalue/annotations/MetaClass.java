package com.slimgears.util.autovalue.annotations;

import com.slimgears.util.reflect.TypeToken;

/**
 * Immutable meta class
 * @param <T> Target immutable class
 */
public interface MetaClass<T> {
    /**
     * Builder class
     * @return builder class
     */
    TypeToken<? extends MetaBuilder<T>> builderClass();

    /**
     * Object type token
     * @return object type token
     */
    TypeToken<T> objectClass();

    /**
     * Class Properties
     * @return meta properties
     */
    Iterable<PropertyMeta<T, ?>> properties();

    /**
     * Returns property by name
     * @param propertyName property name
     * @param <V> property value type
     * @return property instance or null if property does not exist
     */
    <V> PropertyMeta<T, V> getProperty(String propertyName);

    /**
     * Creates new empty builder instance
     * @param <B> Builder type
     * @return Builder instance
     */
    <B extends MetaBuilder<T>> B createBuilder();

    /**
     * Returns metaclasses, on which is meta class depends
     * @return Iterable of meta classes (empty iterable if no dependencies)
     */
    default Iterable<MetaClass<?>> dependencies() {
        return MetaClasses.dependencies(this);
    }

    /**
     * Target class simple name
     * @return Target class' simple name
     */
    default String simpleName() {
        return asClass().getSimpleName();
    }

    /**
     * Returns target class
     * @return target class
     */
    default Class<T> asClass() {
        return objectClass().asClass();
    }
}
