package com.slimgears.util.autovalue.annotations;

import com.google.common.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("UnstableApiUsage")
public class PropertyType<T> {
    private final TypeToken<T> typeToken;

    @SuppressWarnings("unchecked")
    protected PropertyType() {
        Type type = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.typeToken = (TypeToken<T>)TypeToken.of(type);
    }

    public TypeToken<T> asToken() {
        return typeToken;
    }
}
