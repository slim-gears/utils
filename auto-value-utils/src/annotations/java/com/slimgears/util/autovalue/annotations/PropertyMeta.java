package com.slimgears.util.autovalue.annotations;

import com.slimgears.util.reflect.TypeToken;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface PropertyMeta<T, V> {
    MetaClass<T> declaringType();
    String name();
    TypeToken<V> type();
    void setValue(MetaBuilder<T> builder, V value);
    V getValue(T instance);

    default void mergeValue(MetaBuilder<T> builder, T instance) {
        Optional
                .ofNullable(getValue(instance))
                .ifPresent(val -> setValue(builder, val));
    }

    static <T extends HasMetaClass<T>, V> PropertyMeta<T, V> create(MetaClass<T> metaClass, String name) {
        return metaClass.getProperty(name);
    }

    static <T extends HasMetaClass<T>, V> PropertyMeta<T, V> create(TypeToken<T> declaringType, String name) {
        return create(MetaClasses.forToken(declaringType), name);
    }

    static <T, V, B extends BuilderPrototype<T, B>> PropertyMeta<T, V> create(MetaClass<T> declaringType, String name, TypeToken<V> type, Function<T, V> getter, BiConsumer<B, V> setter) {
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
            public boolean equals(Object obj) {
                return obj instanceof PropertyMeta
                        && Objects.equals(declaringType, ((PropertyMeta) obj).declaringType())
                        && Objects.equals(name, ((PropertyMeta) obj).name());
            }

            @Override
            public int hashCode() {
                return Objects.hash(declaringType, name);
            }
        };
    }
}
