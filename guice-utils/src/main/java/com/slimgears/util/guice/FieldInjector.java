package com.slimgears.util.guice;

import com.google.inject.*;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.slimgears.util.stream.Lazy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class FieldInjector<A extends Annotation, T> implements TypeListener {
    private final Class<A> annotationClass;
    private final Class<T> injectionClass;
    private final Lazy<Injector> injectorProvider;
    private final Factory<A, T> factory;

    private interface Factory<A extends Annotation, T> {
        T createInstance(A annotation, Field field, Injector injector);
    }

    private FieldInjector(Class<A> annotationClass, Class<T> injectionClass, Factory<A, T> factory, Provider<Injector> injectorProvider) {
        this.annotationClass = annotationClass;
        this.injectionClass = injectionClass;
        this.factory = factory;
        this.injectorProvider = Lazy.of(injectorProvider::get);
    }

    @Override
    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
        fieldsOfClass(type.getRawType())
                .filter(f -> f.isAnnotationPresent(annotationClass) && f.getType().isAssignableFrom(injectionClass))
                .forEach(f -> encounter.register(new FieldMembersInjector<>(f)));
    }

    private Stream<Field> fieldsOfClass(Class<?> cls) {
        Stream<Field> fields = Arrays.stream(cls.getDeclaredFields());
        return Optional.ofNullable(cls.getSuperclass())
                .map(this::fieldsOfClass)
                .map(f -> Stream.concat(fields, f))
                .orElse(fields);
    }

    private class FieldMembersInjector<I> implements MembersInjector<I> {
        private final Field field;
        private final Lazy<T> value;

        private FieldMembersInjector(Field field) {
            this.field = field;
            this.field.setAccessible(true);
            this.value = Lazy.of(() -> factory.createInstance(
                    field.getAnnotation(annotationClass),
                    field,
                    injectorProvider.get()));
        }

        @Override
        public void injectMembers(I instance) {
            try {
                field.set(instance, value.get());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class AnnotatedInjectorConfiguratorWithFactory<A extends Annotation, T> {
        private final Class<A> annotationClass;
        private final Class<T> injectionClass;
        private final Factory<A, T> factory;

        private AnnotatedInjectorConfiguratorWithFactory(Class<A> annotationClass, Class<T> injectionClass, Factory<A, T> factory) {
            this.annotationClass = annotationClass;
            this.injectionClass = injectionClass;
            this.factory = factory;
        }

        public Module toModule() {
            return new AbstractModule() {
                @Override
                protected void configure() {
                    bindListener(Matchers.any(), new FieldInjector<>(annotationClass, injectionClass, factory, getProvider(Injector.class)));
                }
            };
        }

        public void install(Binder binder) {
            binder.install(toModule());
        }
    }

    public static class AnnotatedInjectorConfigurator<A extends Annotation, T> {
        private final Class<A> annotationClass;
        private final Class<T> injectionClass;

        private AnnotatedInjectorConfigurator(Class<A> annotationClass, Class<T> injectionClass) {
            this.annotationClass = annotationClass;
            this.injectionClass = injectionClass;
        }

        public AnnotatedInjectorConfiguratorWithFactory<A, T> useFactory(Factory<A, T> factory) {
            return new AnnotatedInjectorConfiguratorWithFactory<>(annotationClass, injectionClass, factory);
        }

        public AnnotatedInjectorConfiguratorWithFactory<A, T> useInjector() {
            return useFactory((a, f, i) -> i.getInstance(injectionClass));
        }

        public AnnotatedInjectorConfiguratorWithFactory<A, T> useInstance(T instance) {
            return useFactory((a, f, i) -> instance);
        }

        public AnnotatedInjectorConfiguratorWithFactory<A, T> useProvider(Provider<T> provider) {
            return useFactory((a, f, i) -> provider.get());
        }

        public AnnotatedInjectorConfiguratorWithFactory<A, T> resolveByClass(Function<Class<?>, T> resolver) {
            return resolveByClass((a, c) -> resolver.apply(c));
        }

        public AnnotatedInjectorConfiguratorWithFactory<A, T> resolveByClass(BiFunction<A, Class<?>, T> resolver) {
            return useFactory((a, f, i) -> resolver.apply(a, f.getDeclaringClass()));
        }

        public AnnotatedInjectorConfiguratorWithFactory<A, T> resolveByField(Function<Field, T> resolver) {
            return resolveByField((a, f) -> resolver.apply(f));
        }

        public AnnotatedInjectorConfiguratorWithFactory<A, T> resolveByField(BiFunction<A, Field, T> resolver) {
            return useFactory((a, f, i) -> resolver.apply(a, f));
        }
    }

    public static class InjectorConfigurator<T> {
        private final Class<T> injectionClass;

        private InjectorConfigurator(Class<T> injectionClass) {
            this.injectionClass = injectionClass;
        }

        public <A extends Annotation> AnnotatedInjectorConfigurator<A, T> toAnnotatedField(Class<A> annotationClass) {
            return new AnnotatedInjectorConfigurator<A, T>(annotationClass, injectionClass);
        }
    }

    public static <T> InjectorConfigurator<T> inject(Class<T> cls) {
        return new InjectorConfigurator<>(cls);
    }
}
