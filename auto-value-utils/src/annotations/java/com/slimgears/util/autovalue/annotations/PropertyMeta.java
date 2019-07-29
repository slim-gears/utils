package com.slimgears.util.autovalue.annotations;

import com.slimgears.util.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Property meta data
 * @param <T> Declaring type
 * @param <V> Value type
 */
public interface PropertyMeta<T, V> {
    /**
     * Declaring meta class
     * @return Meta class
     */
    MetaClass<T> declaringType();

    /**
     * Property name
     * @return property name
     */
    String name();

    /**
     * Value type
     * @return Value type
     */
    TypeToken<V> type();

    /**
     * Sets property value in object builder
     * @param builder builder instance
     * @param value value
     */
    void setValue(MetaBuilder<T> builder, V value);

    /**
     * Returns property value from object
     * @param instance object instance
     * @return property value
     */
    V getValue(T instance);

    /**
     * Returns property annotation by type
     * @param annotationClass annotation type
     * @param <A> annotation type
     * @return annotation instance if exists, otherwise null
     */
    <A extends Annotation> A getAnnotation(Class<A> annotationClass);

    /**
     * Returns whether the property is mergeable
     * @return true if mergeable, otherwise false
     */
    default boolean isMergeable() {
        return hasAnnotation(Mergeable.class);
    }

    /**
     * Checks whether the property has annotation with specified annotation type
     * @param annotationClass annotation type
     * @return true if annotation with specified type exists
     */
    default boolean hasAnnotation(Class<? extends Annotation> annotationClass) {
        return getAnnotation(annotationClass) != null;
    }

//    static <T extends HasMetaClass<T>, V> PropertyMeta<T, V> create(MetaClass<T> metaClass, String name) {
//        return metaClass.getProperty(name);
//    }
//
//    static <T extends HasMetaClass<T>, V> PropertyMeta<T, V> create(TypeToken<T> declaringType, String name) {
//        return create(MetaClasses.forToken(declaringType), name);
//    }

    static <T, V, B extends BuilderPrototype<T, B>> PropertyMeta<T, V> create(
            MetaClass<T> declaringType,
            String name,
            TypeToken<V> type,
            Function<T, V> getter,
            BiConsumer<B, V> setter) {
        return create(declaringType, name, type, getter, setter, name);
    }

    static <T, V, B extends BuilderPrototype<T, B>> PropertyMeta<T, V> create(
            MetaClass<T> declaringType,
            String name,
            TypeToken<V> type,
            Function<T, V> getter,
            BiConsumer<B, V> setter,
            String getterMethodName) {

        Supplier<Method> lazyMethod = AtomicLazy.of(() -> declaringType.objectClass().asClass().getMethod(getterMethodName));

        return new PropertyMeta<T, V>() {
            @Override
            public MetaClass<T> declaringType() {
                return declaringType;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public TypeToken<V> type() {
                return type;
            }

            @Override
            public void setValue(MetaBuilder<T> builder, V value) {
                //noinspection unchecked
                setter.accept((B)builder, value);
            }

            @Override
            public V getValue(T instance) {
                return getter.apply(instance);
            }

            @Override
            public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
                return lazyMethod.get().getAnnotation(annotationClass);
            }

            @Override
            public boolean equals(Object obj) {
                return obj instanceof PropertyMeta
                        && Objects.equals(declaringType.objectClass().asClass(), ((PropertyMeta) obj).declaringType().objectClass().asClass())
                        && Objects.equals(name, ((PropertyMeta) obj).name());
            }

            @Override
            public int hashCode() {
                return Objects.hash(declaringType.objectClass().asClass(), name);
            }

            @Override
            public String toString() {
                return declaringType.objectClass().asClass().getSimpleName() + "." + name;
            }
        };
    }
}
