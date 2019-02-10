package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

import java.io.IOException;
import java.util.Optional;

public class ExpressionTypeResolver implements TypeIdResolver {
    private JavaType baseType;

    @Override
    public void init(JavaType baseType) {
        this.baseType = baseType;
    }

    @Override
    public String idFromValue(Object value) {
        String id = ((Expression)value).type().name();
        return Optional.of(id)
                .filter(n -> !n.isEmpty())
                .map(n -> Character.toLowerCase(n.charAt(0)) + n.substring(1))
                .orElse(id);
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return idFromValue(value);
    }

    @Override
    public String idFromBaseType() {
        return null;
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) throws IOException {
        return Optional.ofNullable(id)
                .filter(n -> !n.isEmpty())
                .map(n -> Character.toUpperCase(n.charAt(0)) + n.substring(1))
                .map(ExpressionType::valueOf)
                .map(ExpressionType::type)
                .map(t -> context.constructSpecializedType(baseType, t))
                .orElseThrow(() -> new IllegalStateException("Cannot recognize type " + id));
    }

    @Override
    public String getDescForKnownTypeIds() {
        return null;
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}
