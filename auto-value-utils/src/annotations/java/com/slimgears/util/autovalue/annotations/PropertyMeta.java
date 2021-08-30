package com.slimgears.util.autovalue.annotations;

import com.google.common.reflect.TypeToken;
import com.slimgears.util.reflect.TypeTokens;
import com.slimgears.util.stream.Lazy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Property meta data
 * @param <T> Declaring type
 * @param <V> Value type
 */
@SuppressWarnings("UnstableApiUsage")
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
        return !hasAnnotation(NonMergeable.class);
    }

    /**
     * Checks whether the property has annotation with specified annotation type
     * @param annotationClass annotation type
     * @return true if annotation with specified type exists
     */
    default boolean hasAnnotation(Class<? extends Annotation> annotationClass) {
        return getAnnotation(annotationClass) != null;
    }

    static <T, V, B extends BuilderPrototype<T, B>> PropertyMeta<T, V> create(
            MetaClass<T> declaringType,
            String name,
            PropertyType<V> type,
            Function<T, V> getter,
            BiConsumer<B, V> setter) {
        return create(declaringType, name, type, getter, setter, name);
    }

    static <T, V, B extends BuilderPrototype<T, B>> PropertyMeta<T, V> create(
            MetaClass<T> declaringType,
            String name,
            PropertyType<V> type,
            Function<T, V> getter,
            BiConsumer<B, V> setter,
            String getterMethodName) {

        Supplier<Method> lazyMethod = Lazy.of(() -> TypeTokens.method(declaringType.asType(), getterMethodName));
        Supplier<ConcurrentMap<Class<?>, Annotation>> lazyAnnotationsMap = Lazy.of(ConcurrentHashMap::new);
        TypeToken<V> typeToken = type.asToken();

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
                return typeToken;
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
                return annotationClass.cast(lazyAnnotationsMap.get()
                        .computeIfAbsent(annotationClass, a -> lazyMethod.get().getAnnotation(annotationClass)));
            }

            @Override
            public boolean equals(Object obj) {
                return obj instanceof PropertyMeta
                        && Objects.equals(declaringType.asType().getRawType(), ((PropertyMeta) obj).declaringType().asType().getRawType())
                        && Objects.equals(name, ((PropertyMeta) obj).name());
            }

            @Override
            public int hashCode() {
                return Objects.hash(declaringType.asType().getRawType(), name);
            }

            @Override
            public String toString() {
                return declaringType.asType().getRawType().getSimpleName() + "." + name;
            }
        };
    }
}
