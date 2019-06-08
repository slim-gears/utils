package com.slimgears.util.autovalue.annotations;

import com.slimgears.util.reflect.TypeToken;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings("WeakerAccess")
public class MetaClasses {
    private final static Map<Class, MetaClass> metaClassMap = new HashMap<>();

    public static <T extends HasMetaClass<T>> MetaClass<T> forClass(Class<T> cls) {
        return forClassUnchecked(cls);
    }

    public static <T extends HasMetaClass<T>> MetaClass<T> forClassUnchecked(Class<?> cls) {
        //noinspection unchecked
        return (MetaClass<T>)metaClassMap.computeIfAbsent(cls, MetaClasses::fromField);
    }

    public static <K, T extends HasMetaClassWithKey<K, T>> MetaClassWithKey<K, T> forClassWithKey(Class<T> cls) {
        //noinspection unchecked
        return (MetaClassWithKey<K, T>)metaClassMap.computeIfAbsent(cls, MetaClasses::fromField);
    }

    public static <T extends HasMetaClass<T>> MetaClass<T> forToken(TypeToken<T> typeToken) {
        return forClass(typeToken.asClass());
    }

    public static <T extends HasMetaClass<T>> MetaClass<T> forTokenUnchecked(TypeToken<?> typeToken) {
        return forClassUnchecked(typeToken.asClass());
    }

    public static Iterable<MetaClass<?>> dependencies(MetaClass<?> metaClass) {
        return StreamSupport
                .stream(metaClass.properties().spliterator(), false)
                .filter(p -> p.type().is(HasMetaClass.class::isAssignableFrom))
                .map(PropertyMeta::type)
                .map(t -> t.as(new TypeToken<HasMetaClass<?>>(){}))
                .map(MetaClasses::forTokenUnchecked)
                .collect(Collectors.toList());
    }

    public static <K, T extends HasMetaClassWithKey<K, T>> MetaClassWithKey<K, T> forTokenWithKey(TypeToken<T> typeToken) {
        return forClassWithKey(typeToken.asClass());
    }

    private static <T extends HasMetaClass<T>> MetaClass<T> fromField(Class<T> cls) {
        try {
            Field field = cls.getField("metaClass");
            field.setAccessible(true);
            //noinspection unchecked
            return (MetaClass<T>)field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
}
