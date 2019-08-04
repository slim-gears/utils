package com.slimgears.util.reflect;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import com.slimgears.util.reflect.internal.Require;
import com.slimgears.util.reflect.internal.TypeTokenParserAdapter;
import com.slimgears.util.stream.Equality;
import com.slimgears.util.stream.Optionals;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
public class TypeTokens {
    public static <T> T newInstance(TypeToken<T> type, Object... args) {
        try {
            return type
                    .constructor(type.getRawType().getConstructor(Arrays.stream(args).map(Object::getClass).toArray(Class[]::new)))
                    .invoke(null, args);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean hasModifier(TypeToken<?> type, Predicate<Integer> modifierPredicate) {
        return modifierPredicate.test(type.getRawType().getModifiers());
    }

    public static boolean isEnum(TypeToken<?> typeToken) {
        return typeToken.getRawType().isEnum();
    }

    public static boolean isInterface(TypeToken<?> typeToken) {
        return typeToken.getRawType().isInterface();
    }

    public static boolean isArray(TypeToken<?> typeToken) {
        return typeToken.getRawType().isArray();
    }

    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> constructor(TypeToken<T> type, Class<?>... argTypes) {
        try {
            return (Constructor<T>) type.getRawType().getConstructor(argTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Method method(TypeToken<T> type, String name, Class<?>... argTypes) {
        try {
            return type.getRawType().getMethod(name, argTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> TypeToken<T> valueOf(String str) {
        return TypeTokenParserAdapter.toTypeToken(str);
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> asClass(TypeToken<T> typeToken) {
        return (Class<T>)typeToken.getRawType();
    }

    public static Collection<TypeToken<?>> arguments(TypeToken<?> type) {
        return Optional.of(type.getType())
                .flatMap(Optionals.ofType(ParameterizedType.class))
                .map(pt -> Arrays.stream(pt.getActualTypeArguments()))
                .orElse(Stream.empty())
                .map(TypeToken::of)
                .collect(ImmutableList.toImmutableList());
    }

    public static ParameterizedBuilder parameterizedBuilder(Class<?> rawType) {
        return new ParameterizedBuilder(rawType);
    }

    @SuppressWarnings("unchecked")
    public static <T> TypeToken<T> ofType(Type type) {
        return (TypeToken<T>)TypeToken.of(type);
    }

    @SuppressWarnings("unchecked")
    public static <T> TypeToken<T> ofParameterized(Class<?> rawType, TypeToken<?>... args) {
        return parameterizedBuilder(rawType).typeArgs(args).build();
    }

    public static <T> TypeToken<T> ofParameterized(Class<?> rawType, Type... args) {
        return parameterizedBuilder(rawType).typeArgs(args).build();
    }

    public static <T> TypeToken<T[]> ofArray(TypeToken<T> elementType) {
        return new TypeToken<T[]>() {}.where(new TypeParameter<T>() {}, elementType);
    }

    @SuppressWarnings("unchecked")
    public static <T> TypeToken<T[]> ofArray(Type argument) {
        return ofArray((TypeToken<T>)TypeToken.of(argument));
    }

    public static TypeToken<?> ofWildcard() {
        return ofWildcard(new Type[0], new Type[0]);
    }

    public static TypeToken<?> ofWildcard(Type[] upperBounds, Type[] lowerBounds) {
        return TypeToken.of(wildcardType(upperBounds, lowerBounds));
    }

    @SuppressWarnings("unchecked")
    public static <T> TypeToken<? extends T> ofExtendsWildcard(TypeToken<T> upperBound) {
        return (TypeToken<? extends T>)ofWildcard(new Type[] {upperBound.getType()}, new Type[0]);
    }

    @SuppressWarnings("unchecked")
    public static <T> TypeToken<? super T> ofSuperWildcard(TypeToken<T> lowerBound) {
        return (TypeToken<? super T>)ofWildcard(new Type[0], new Type[] {lowerBound.getType()});
    }

    public static class ParameterizedBuilder {
        private final Class rawType;
        private final AtomicReference<Type> ownerType = new AtomicReference<>();
        private final List<Type> params = new ArrayList<>();

        public ParameterizedBuilder(Class rawType) {
            this.rawType = rawType;
        }

        public ParameterizedBuilder ownerType(Type owner) {
            ownerType.set(owner);
            return this;
        }

        public ParameterizedBuilder typeArgs(Type... types) {
            params.addAll(Arrays.asList(types));
            return this;
        }

        public ParameterizedBuilder typeArgs(TypeToken<?>... tokens) {
            Arrays.stream(tokens)
                    .map(TypeToken::getType)
                    .forEach(params::add);
            return this;
        }

        @SuppressWarnings("unchecked")
        public <T> TypeToken<T> build() {
            Require.argument(
                    rawType.getTypeParameters().length == params.size(),
                    () -> String.format("Type parameter number mismatch (expected: %d, actual: %d)",
                            rawType.getTypeParameters().length,
                            params.size()));

            return (TypeToken<T>)TypeToken.of(buildType());
        }

        private Type buildType() {
            if (params.isEmpty()) {
                return rawType;
            }

            Type[] args = params.toArray(new Type[0]);
            return parameterizedType(rawType, ownerType.get(), args);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> TypeToken<T> eliminateTypeVars(TypeToken<T> type) {
        return (TypeToken<T>)TypeToken.of(TypeVarContext.runInContext(() -> eliminateTypeVars(type.getType())));
    }

    public static boolean hasTypeVars(TypeToken<?> type) {
        return hasTypeVars(type.getType());
    }

    private static boolean hasTypeVars(Type... types) {
        return Arrays.stream(types).anyMatch(TypeTokens::hasTypeVars);
    }

    private static boolean hasTypeVars(Type type) {
        if (type instanceof TypeVariable) {
            return true;
        }
        if (type instanceof GenericArrayType) {
            return hasTypeVars(((GenericArrayType)type).getGenericComponentType());
        }
        if (type instanceof ParameterizedType) {
            return hasTypeVars(((ParameterizedType)type).getActualTypeArguments());
        }
        if (type instanceof WildcardType) {
            return hasTypeVars(((WildcardType)type).getLowerBounds()) ||
                    hasTypeVars(((WildcardType)type).getUpperBounds());
        }
        return false;
    }

    private static WildcardType wildcardType(Type[] upperBounds, Type[] lowerBounds) {
        Equality.Checker<WildcardType> equalityChecker = Equality.builder(WildcardType.class)
                .add(WildcardType::getLowerBounds)
                .add(WildcardType::getUpperBounds)
                .build();

        return new WildcardType() {
            @Override
            public Type[] getUpperBounds() {
                return upperBounds;
            }

            @Override
            public Type[] getLowerBounds() {
                return lowerBounds;
            }

            @Override
            public boolean equals(Object obj) {
                return equalityChecker.equals(this, obj);
            }

            @Override
            public int hashCode() {
                return equalityChecker.hashCode(this);
            }

            @Override
            public String toString() {
                Type[] effectiveUpperBounds = Arrays
                        .stream(upperBounds)
                        .filter(ub -> ub != Object.class)
                        .toArray(Type[]::new);

                if (lowerBounds.length > 0) {
                    return "? super " + Arrays.stream(lowerBounds).map(Type::getTypeName).collect(Collectors.joining(" & "));
                } else if (effectiveUpperBounds.length > 0) {
                    return "? extends " + Arrays.stream(upperBounds).map(Type::getTypeName).collect(Collectors.joining(" & "));
                } else {
                    return "?";
                }
            }
        };
    }

    private static Type parameterizedType(Type rawType, Type ownerType, Type[] args) {
        if (args.length == 0) {
            return rawType;
        }

        Equality.Checker<ParameterizedType> equalityChecker = Equality.builder(ParameterizedType.class)
                .add(ParameterizedType::getRawType)
                .add(ParameterizedType::getOwnerType)
                .add(ParameterizedType::getActualTypeArguments)
                .build();

        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return args;
            }

            @Override
            public Type getRawType() {
                return rawType;
            }

            @Override
            public Type getOwnerType() {
                return ownerType;
            }

            @Override
            public String toString() {
                return rawType.getTypeName() + Arrays.stream(args).map(Type::getTypeName).collect(Collectors.joining(", ", "<", ">"));
            }

            @Override
            public boolean equals(Object obj) {
                return equalityChecker.equals(this, obj);
            }

            @Override
            public int hashCode() {
                return equalityChecker.hashCode(this);
            }
        };
    }

    private static GenericArrayType arrayType(Type componentType) {
        return new GenericArrayType() {
            @Override
            public Type getGenericComponentType() {
                return componentType;
            }

            @Override
            public String toString() {
                return componentType.getTypeName() + "[]";
            }

            @Override
            public boolean equals(Object obj) {
                return obj instanceof GenericArrayType &&
                        componentType.equals(((GenericArrayType) obj).getGenericComponentType());
            }

            @Override
            public int hashCode() {
                return componentType.hashCode();
            }
        };
    }

    private static Type[] eliminateTypeVars(Type[] types) {
        return Arrays.stream(types).map(TypeTokens::eliminateTypeVars).toArray(Type[]::new);
    }

    private static Type eliminateTypeVars(Type type) {
        if (type instanceof TypeVariable) {
            return wildcardType(
                    TypeVarContext.visit((TypeVariable)type)
                            ? eliminateTypeVars(((TypeVariable) type).getBounds())
                            : new Type[]{Object.class},
                    new Type[0]);
        }
        if (type instanceof ParameterizedType) {
            return parameterizedType(
                    eliminateTypeVars(((ParameterizedType) type).getRawType()),
                    eliminateTypeVars(((ParameterizedType) type).getOwnerType()),
                    eliminateTypeVars(((ParameterizedType)type).getActualTypeArguments()));
        }
        if (type instanceof GenericArrayType) {
            return arrayType(eliminateTypeVars(((GenericArrayType)type).getGenericComponentType()));
        }
        return type;
    }

    private static class TypeVarContext {
        private final static ThreadLocal<TypeVarContext> instance = new ThreadLocal<>();
        private final Set<TypeVariable> visitedVars;

        public TypeVarContext(TypeVarContext parent) {
            this.visitedVars = Optional.ofNullable(parent)
                    .map(p -> p.visitedVars)
                    .map(HashSet::new)
                    .orElseGet(HashSet::new);
        }

        public static boolean visit(TypeVariable typeVariable) {
            return Objects.requireNonNull(instance.get()).visitedVars.add(typeVariable);
        }

        public static <T> T runInContext(Callable<T> callable) {
            TypeVarContext prev = instance.get();
            instance.set(new TypeVarContext(prev));
            try {
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                instance.set(prev);
            }
        }
    }
}
