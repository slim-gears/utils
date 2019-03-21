package com.slimgears.util.autovalue.annotations;

import com.slimgears.util.reflect.TypeToken;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MetaClasses {
    private final static Map<Class, MetaClass> metaClassMap = new HashMap<>();

    public static <T extends HasMetaClass<T>> MetaClass<T> forClass(Class<T> cls) {
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
