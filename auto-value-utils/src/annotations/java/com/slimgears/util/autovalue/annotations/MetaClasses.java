package com.slimgears.util.autovalue.annotations;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MetaClasses {
    private final static Map<Class, MetaClass> metaClassMap = new HashMap<>();

    public static <T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>> MetaClass<T, TB> forClass(Class<T> cls) {
        //noinspection unchecked
        return (MetaClass<T, TB>)metaClassMap.computeIfAbsent(cls, MetaClasses::fromField);
    }

    private static <T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>> MetaClass<T, TB> fromField(Class<T> cls) {
        try {
            Field field = cls.getField("metaClass");
            field.setAccessible(true);
            //noinspection unchecked
            return (MetaClass<T, TB>)field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
}
