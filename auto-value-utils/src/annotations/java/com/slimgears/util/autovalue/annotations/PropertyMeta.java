package com.slimgears.util.autovalue.annotations;

import com.slimgears.util.reflect.TypeToken;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface PropertyMeta<T, B, V> {
    TypeToken<T> declaringType();
    String name();
    TypeToken<V> type();
    void setValue(B builder, V value);
    V getValue(T instance);

    default void mergeValue(B builder, T instance) {
        Optional
                .ofNullable(getValue(instance))
                .ifPresent(val -> setValue(builder, val));
    }

    static <T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>, V> PropertyMeta<T, TB, V> create(MetaClass<T, TB> metaClass, String name) {
        return metaClass.getProperty(name);
    }

    static <T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>, V> PropertyMeta<T, TB, V> create(TypeToken<T> declaringType, String name) {
        return create(MetaClasses.forToken(declaringType), name);
    }

    static <T, B, V> PropertyMeta<T, B, V> create(TypeToken<T> declaringType, String name, TypeToken<V> type, Function<T, V> getter, BiConsumer<B, V> setter) {
        return new PropertyMeta<T, B, V>() {
            @Override
            public TypeToken<T> declaringType() {
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
            public void setValue(B builder, V value) {
                setter.accept(builder, value);
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
