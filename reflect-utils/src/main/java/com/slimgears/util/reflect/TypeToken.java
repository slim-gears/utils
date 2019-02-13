package com.slimgears.util.reflect;

import com.slimgears.util.reflect.internal.CanonicalGenericArrayType;
import com.slimgears.util.reflect.internal.CanonicalParameterizedType;
import com.slimgears.util.reflect.internal.CanonicalWildcardType;
import com.slimgears.util.reflect.internal.Require;
import com.slimgears.util.reflect.internal.TypeTokenParserAdapter;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TypeToken<T> {
    private final Type type;

    public static TypeToken ofWildcard(Type[] lowerBounds, Type[] upperBounds) {
        return TypeToken.ofType(new CanonicalWildcardType(lowerBounds, upperBounds));
    }

    public static TypeToken ofWildcard() {
        return TypeToken.ofType(new CanonicalWildcardType(new Type[0], new Type[0]));
    }

    public static TypeToken ofWildcardUpper(Type... upperBounds) {
        return TypeToken.ofType(new CanonicalWildcardType(new Type[0], upperBounds));
    }

    public static TypeToken ofWildcardLower(Type... lowerBounds) {
        return TypeToken.ofType(new CanonicalWildcardType(new Type[0], lowerBounds));
    }

    public interface Constructor<T> {
        T newInstance(Object... args);
    }

    public interface Method<T, R> {
        R invoke(T receiver, Object... args);
    }

    protected TypeToken() {
        this.type = Optional
                .ofNullable(getClass().getGenericSuperclass())
                .filter(ParameterizedType.class::isInstance)
                .map(ParameterizedType.class::cast)
                .map(pt -> pt.getActualTypeArguments()[0])
                .map(CanonicalType::of)
                .orElseGet(() -> getClass().getSuperclass());
    }

    public boolean isArray() {
        return type instanceof GenericArrayType;
    }

    public boolean is(Predicate<Class> predicate) {
        return Optional.ofNullable(asClass()).map(predicate::test).orElse(false);
    }

    public boolean isEnum() {
        return is(Class::isEnum);
    }

    public boolean isInterface() {
        return is(Class::isInterface);
    }

    public boolean hasModifier(Function<Integer, Boolean> modifierTester) {
        return Optional.ofNullable(asClass())
                .map(Class::getModifiers)
                .map(modifierTester)
                .orElse(false);
    }

    public TypeToken<T[]> toArray() {
        return ofArray(this);
    }

    public Type type() {
        return this.type;
    }

    public TypeToken<?>[] typeArguments() {
        if (!(type instanceof ParameterizedType)) {
            return new TypeToken[0];
        }
        //noinspection ResultOfMethodCallIgnored
        return Arrays.stream(((ParameterizedType)type).getActualTypeArguments())
                .map(TypeToken::new)
                .toArray(TypeToken[]::new);
    }

    public Class<T> asClass() {
        //noinspection unchecked
        return type instanceof Class
                ? (Class<T>)type
                : (Class<T>)((CanonicalType)type).asClass();
    }

    public T newInstance() {
        return constructor().newInstance();
    }

    public Method<T, ?> method(String name, Class<?> paramTypes) {
        try {
            java.lang.reflect.Method method = asClass().getMethod(name, paramTypes);
            return (obj, args) -> {
                try {
                    return method.invoke(obj, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            };
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Constructor<T> constructor(Class<?>... paramTypes) {
        try {
            java.lang.reflect.Constructor<T> ctor = asClass().getConstructor(paramTypes);
            return args -> {
                try {
                    return ctor.newInstance(args);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            };
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private TypeToken(Type type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof TypeToken) && type.equals(((TypeToken)obj).type);
    }

    @Override
    public String toString() {
        return type.getTypeName();
    }

    public static <T> TypeToken<T[]> ofArray(Class<T> componentType) {
        return new TypeToken<>(new CanonicalGenericArrayType(CanonicalType.of(componentType)));
    }

    public static <T> TypeToken<T[]> ofArray(TypeToken<T> componentToken) {
        return new TypeToken<>(new CanonicalGenericArrayType(componentToken.type()));
    }

    public static <T> TypeToken<T> of(Class<T> cls) {
        return new TypeToken<>(CanonicalType.of(cls));
    }

    public static <T> TypeToken<T> ofType(Type type) {
        return new TypeToken<>(type);
    }

    public static <T> TypeToken<T> ofParameterized(Class rawClass, Class... args) {
        //noinspection unchecked
        return (args.length == 0)
                ? of(rawClass)
                : parameterizedBuilder(rawClass).typeArgs(args).build();
    }

    public static <T> TypeToken<T> ofParameterized(Class rawClass, TypeToken<?>... args) {
        //noinspection unchecked
        return (args.length == 0)
                ? of(rawClass)
                : parameterizedBuilder(rawClass).typeArgs(args).build();
    }

    public static ParameterizedBuilder parameterizedBuilder(Class rawClass) {
        return new ParameterizedBuilder(rawClass);
    }

    public static class ParameterizedBuilder {
        private final Class rawClass;
        private final List<Type> params = new ArrayList<>();

        public ParameterizedBuilder(Class rawClass) {
            this.rawClass = rawClass;
        }

        public ParameterizedBuilder typeArgs(Type... types) {
            Arrays.stream(types)
                    .map(CanonicalType::of)
                    .forEach(params::add);
            return this;
        }

        public ParameterizedBuilder typeArgs(TypeToken<?>... tokens) {
            Arrays.stream(tokens)
                    .map(token -> token.type)
                    .map(CanonicalType::of)
                    .forEach(params::add);
            return this;
        }

        public <T> TypeToken<T> build() {
            Require.argument(
                    rawClass.getTypeParameters().length == params.size(),
                    () -> String.format("Type parameter number mismatch (expected: %d, actual: %d)",
                            rawClass.getTypeParameters().length,
                            params.size()));

            Type[] args = params.toArray(new Type[0]);
            return new TypeToken<>(new CanonicalParameterizedType(rawClass, rawClass.getEnclosingClass(), params.toArray(args)));
        }
    }

    public TypeToken<?> eliminateTypeVars() {
        return TypeToken.ofType(eliminateTypeVars(type));
    }

    private static Type eliminateTypeVars(Type type) {
        if (type instanceof TypeVariable) {
            return new CanonicalWildcardType(new Type[0], ((TypeVariable) type).getBounds());
        }
        if (type instanceof ParameterizedType) {
            return new CanonicalParameterizedType(
                    ((ParameterizedType) type).getRawType(),
                    ((ParameterizedType) type).getOwnerType(),
                    Arrays.stream(((ParameterizedType)type).getActualTypeArguments())
                    .map(TypeToken::eliminateTypeVars)
                    .toArray(Type[]::new));
        }
        if (type instanceof GenericArrayType) {
            return new CanonicalGenericArrayType(
                    eliminateTypeVars(((GenericArrayType)type).getGenericComponentType()));
        }
        return type;
    }

    public static <T> TypeToken<T> valueOf(String str) {
        //noinspection unchecked
        return TypeTokenParserAdapter.toTypeToken(str);
    }
}
