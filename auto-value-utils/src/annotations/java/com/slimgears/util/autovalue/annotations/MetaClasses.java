package com.slimgears.util.autovalue.annotations;

import com.google.common.reflect.TypeToken;
import com.slimgears.util.reflect.TypeTokens;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings({"WeakerAccess", "UnstableApiUsage"})
public class MetaClasses {
    private final static Map<Class<?>, MetaClass<?>> metaClassMap = new HashMap<>();

    public static <T extends HasMetaClass<T>> MetaClass<T> forClass(Class<T> cls) {
        return forClassUnchecked(cls);
    }

    @SuppressWarnings("unchecked")
    public static <T> MetaClass<T> forClassUnchecked(Class<?> cls) {
        synchronized (metaClassMap) {
            if (metaClassMap.get(cls) == null) {
                MetaClass<T> newValue = MetaClasses.fromField(cls);
                if (newValue != null) {
                    metaClassMap.put(cls, newValue);
                }
            }
            return (MetaClass<T>)metaClassMap.get(cls);
        }
    }

    public static <K, T extends HasMetaClassWithKey<K, T>> MetaClassWithKey<K, T> forClassWithKey(Class<T> cls) {
        return forClassWithKeyUnchecked(cls);
    }

    @SuppressWarnings("unchecked")
    public static <K, T> MetaClassWithKey<K, T> forClassWithKeyUnchecked(Class<?> cls) {
        return (MetaClassWithKey<K, T>) MetaClasses.forClassUnchecked(cls);
    }

    public static <T extends HasMetaClass<T>> MetaClass<T> forToken(TypeToken<T> typeToken) {
        return forClass(TypeTokens.asClass(typeToken));
    }

    public static <T> MetaClass<T> forTokenUnchecked(TypeToken<?> typeToken) {
        return forClassUnchecked(TypeTokens.asClass(typeToken));
    }

    public static Iterable<MetaClass<?>> dependencies(MetaClass<?> metaClass) {
        return StreamSupport
                .stream(metaClass.properties().spliterator(), false)
                .filter(p -> p.type().isSubtypeOf(HasMetaClass.class))
                .map(PropertyMeta::type)
                .map(MetaClasses::forTokenUnchecked)
                .collect(Collectors.toList());
    }

    public static <K, T extends HasMetaClassWithKey<K, T>> MetaClassWithKey<K, T> forTokenWithKey(TypeToken<T> typeToken) {
        return forClassWithKey(TypeTokens.asClass(typeToken));
    }

    public static <K, T> MetaClassWithKey<K, T> forTokenWithKeyUnchecked(TypeToken<?> typeToken) {
        return forClassWithKeyUnchecked(TypeTokens.asClass(typeToken));
    }

    public static <T extends HasMetaClass<T>> T merge(@Nonnull T first, T second) {
        return merge(first.metaClass(), first, second);
    }

    @SuppressWarnings("unchecked")
    public static <T> T merge(MetaClass<T> metaClass, T first, T second) {
        MetaBuilder<T> builder = ((HasMetaClass<T>)first).toBuilder();
        //noinspection ReplaceInefficientStreamCount
        return StreamSupport.stream(metaClass.properties().spliterator(), false)
                .map(p -> mergeProperty(p, builder, first, second))
                .filter(Boolean::booleanValue)
                .count() > 0
                ? builder.build()
                : first;
    }

    @SuppressWarnings("unchecked")
    static <T, V> boolean mergeProperty(PropertyMeta<T, V> property, MetaBuilder<T> builder, T first, T second) {
        V secondVal = property.getValue(second);
        if (!property.isMergeable()) {
            property.setValue(builder, secondVal);
            return true;
        }

        if (secondVal != null) {
            if (secondVal instanceof HasMetaClass) {
                V firstVal = property.getValue(first);
                if (firstVal != null) {
                    secondVal = merge(((HasMetaClass<V>)firstVal).metaClass(), firstVal, secondVal);
                }
            }
            property.setValue(builder, secondVal);
            return true;
        }
        return false;
    }

    private static <T> MetaClass<T> fromField(Class<?> cls) {
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
