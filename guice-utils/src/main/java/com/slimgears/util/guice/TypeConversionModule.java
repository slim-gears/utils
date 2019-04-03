/**
 *
 */
package com.slimgears.util.guice;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Primitives;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeConverter;
import com.google.inject.spi.TypeConverterBinding;
import com.slimgears.util.stream.Safe;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TypeConversionModule extends AbstractModule {
    @Override
    protected void configure() {
        Provider<Injector> injector = getProvider(Injector.class);
        Provider<Set<TypeConverterBinding>> converterBindings = () -> injector.get().getTypeConverterBindings();

        convertToTypes(isArray(), arrayConverter(getProvider(Injector.class)));
        convertToTypes(is(Set.class), collectionConverter(converterBindings, ImmutableSet::copyOf));
        convertToTypes(is(ImmutableSet.class), collectionConverter(converterBindings, ImmutableSet::copyOf));
        convertToTypes(is(List.class), collectionConverter(converterBindings, ImmutableList::copyOf));
        convertToTypes(is(ImmutableList.class), collectionConverter(converterBindings, ImmutableList::copyOf));
        convertToTypes(is(Collection.class), collectionConverter(converterBindings, ImmutableList::copyOf));
        convertToTypes(is(ImmutableCollection.class), collectionConverter(converterBindings, ImmutableList::copyOf));
        convertToTypes(isOnly(Pattern.class), converter(Pattern::compile));
        convertToTypes(isOnly(Path.class), converter(Paths::get));
        convertToTypes(isOnly(File.class), converter(File::new));
        convertToTypes(isOnly(URI.class), converter(URI::create));
        convertToTypes(isOnly(URL.class), converter(Safe.ofFunction(URL::new)));
        convertToTypes(isOnly(Duration.class), converter(Duration::parse));
    }

    public static Builder builder() {
        return new Builder();
    }

    private static Object convertArray(String value, TypeLiteral<?> toType) {
        return Stream.of(value.split(",")).map(String::trim).toArray(String[]::new);
    }

    interface CollectionFactory {
        <T> Collection<?> toCollection(Collection<? extends T> items);
    }

    private static TypeConverter collectionConverter(Provider<Set<TypeConverterBinding>> converterBindings,
                                           CollectionFactory collectionFactory) {
        return (value, toType) -> {
            Type elementClass = ((ParameterizedType)toType.getType()).getActualTypeArguments()[0];
            TypeLiteral<?> elementType = TypeLiteral.get(elementClass);

            if (value.isEmpty()) {
                return collectionFactory.toCollection(Collections.emptyList());
            }

            String[] items = Stream.of(value.split(",")).map(String::trim).toArray(String[]::new);
            if (elementClass.equals(String.class)) {
                return collectionFactory.toCollection(Arrays.asList(items));
            }

            TypeConverter typeConverter = findConverter(elementType, converterBindings.get());
            return collectionFactory.toCollection(Stream.of(items)
                    .map(str -> typeConverter.convert(str, elementType))
                    .collect(Collectors.toList()));
        };
    }

    private static TypeConverter arrayConverter(Provider<Injector> injector) {
        return (value, toType) -> {
            if (value == null) {
                return null;
            }

            Class<?> elementClass = toType.getRawType().getComponentType();
            Class<?> boxedElementClass = Primitives.wrap(elementClass);
            TypeLiteral<?> elementType = TypeLiteral.get(boxedElementClass);

            if (value.isEmpty()) {
                return Array.newInstance(elementClass, 0);
            }

            String[] items = Stream.of(value.split(",")).map(String::trim).toArray(String[]::new);

            if (elementClass.equals(String.class)) {
                return items;
            }

            TypeConverter typeConverter = findConverter(elementType, injector.get().getTypeConverterBindings());
            return convertArray(items, elementClass, typeConverter);
        };
    }

    private static TypeConverter findConverter(TypeLiteral<?> toType, Set<TypeConverterBinding> converterBindings) {
        return converterBindings
                .stream()
                .filter(b -> b.getTypeMatcher().matches(toType))
                .map(TypeConverterBinding::getTypeConverter)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cannot covert string to " + toType.getRawType().getName()));
    }

    private static <T> Object convertArray(String[] items, Class<T> elementType, TypeConverter typeConverter) {
        TypeLiteral<?> elementTypeLiteral = TypeLiteral.get(elementType);
        //noinspection unchecked
        Object array = Array.newInstance(elementType, items.length);
        //noinspection unchecked
        IntStream.range(0, items.length)
                .forEach(i -> Array.set(array, i, typeConverter.convert(items[i], elementTypeLiteral)));
        return array;
    }

    public static Matcher<TypeLiteral<?>> is(Class<?> cls) {
        return new AbstractMatcher<TypeLiteral<?>>() {
            @Override
            public boolean matches(TypeLiteral<?> typeLiteral) {
                return typeLiteral.getRawType().equals(cls);
            }
        };
    }

    public static Matcher<TypeLiteral<?>> isArray() {
        return is(Class::isArray);
    }

    public static Matcher<TypeLiteral<?>> isEnum() {
        return is(Class::isEnum);
    }

    public static <T> Matcher<TypeLiteral<? extends T>> is(Predicate<Class<?>> classPredicate) {
        return new AbstractMatcher<TypeLiteral<? extends T>>() {
            @Override
            public boolean matches(TypeLiteral<? extends T> typeLiteral) {
                return classPredicate.test(typeLiteral.getRawType());
            }
        };
    }

    public static TypeConverter converter(Function<String, ?> converter) {
        return (str, type) -> converter.apply(str);
    }

    public static Matcher<Object> isOnly(Class<?> cls) {
        return Matchers.only(TypeLiteral.get(cls));
    }

    public static Matcher<TypeLiteral<?>> isAssignableFrom(Class<?> cls) {
        return new AbstractMatcher<TypeLiteral<?>>() {
            @Override
            public boolean matches(TypeLiteral<?> typeLiteral) {
                return cls.isAssignableFrom(typeLiteral.getRawType());
            }
        };
    }

    public static class Builder {
        private final Collection<Consumer<Binder>> bindings = new ArrayList<>();

        public interface ConversionBuilder<T> {
            Builder convert(Function<String, T> converter);
        }

        class InternalConversionBuilder<T> implements ConversionBuilder<T> {
            private final Predicate<? super TypeLiteral<T>> predicate;

            InternalConversionBuilder(Predicate<TypeLiteral<T>> predicate) {
                this.predicate = predicate;
            }

            @Override
            public Builder convert(Function<String, T> converter) {
                bindings.add(binder -> binder.convertToTypes(toMatcher(predicate), (str, type) -> converter.apply(str)));
                return Builder.this;
            }
        }

        @SuppressWarnings("unchecked")
        public <T> ConversionBuilder<T> matchClass(Predicate<Class<T>> classPredicate) {
            return matchTypeLiteral(tl -> classPredicate.test((Class<T>)tl.getRawType()));
        }

        public <T> ConversionBuilder<T> matchTypeLiteral(Predicate<TypeLiteral<T>> typeLiteralPredicate) {
            return new InternalConversionBuilder<>(typeLiteralPredicate);
        }

        public <T> ConversionBuilder<T> isExactly(Class<T> cls) {
            return matchClass(cls::equals);
        }

        public <T> ConversionBuilder<T[]> isArrayOf(Class<T> elementType) {
            return matchClass(cls -> cls.isArray() && elementType.equals(cls.getComponentType()));
        }

        public <T> ConversionBuilder<T> isAssignableFrom(Class<?> cls) {
            return matchClass(cls::isAssignableFrom);
        }

        public <T> ConversionBuilder<Collection<? extends T>> matchCollectionOf(Class<T> elementType) {
            return matchClass(Collection.class::isAssignableFrom);
        }

        public Module build() {
            return new AbstractModule() {
                @Override
                protected void configure() {
                    bindings.forEach(b -> b.accept(binder()));
                }
            };
        }
    }

    private static <T> Matcher<? super TypeLiteral<?>> toMatcher(Predicate<? super TypeLiteral<T>> predicate) {
        return new AbstractMatcher<TypeLiteral<?>>() {
            @SuppressWarnings("unchecked")
            @Override
            public boolean matches(TypeLiteral<?> typeLiteral) {
                return predicate.test((TypeLiteral<T>)typeLiteral);
            }
        };
    }
}
