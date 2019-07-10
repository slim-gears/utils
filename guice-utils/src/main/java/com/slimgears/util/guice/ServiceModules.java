package com.slimgears.util.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Scope;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.slimgears.util.stream.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("WeakerAccess")
public class ServiceModules {
    private final static Logger LOG = LoggerFactory.getLogger(ServiceModules.class);

    public static <S> Module forServiceSet(Class<S> serviceClass) {
        return builder(serviceClass).build(toServiceSet());
    }

    public static <S> Module forNamedServiceMap(Class<S> serviceClass) {
        return builder(serviceClass).build(toNamedServiceMap());
    }

    public static <S, F> Module forNamedServiceFactoryMap(Class<S> serviceClass, Class<F> serviceFactoryClass) {
        return builder(serviceClass).build(toNamedServiceFactoryMap(serviceFactoryClass));
    }

    public static <K, S> Module forServiceMap(Class<K> keyClass, Class<S> serviceClass, Function<Class<? extends S>, K> keyGetter) {
        return builder(serviceClass).build(toServiceMap(keyClass, keyGetter));
    }

    public static <S> Module forServiceMap(Class<S> serviceClass) {
        return builder(serviceClass).build(toServiceMap(String.class, Class::getSimpleName));
    }

    public static <S> Builder<S> builder(Class<S> serviceClass) {
        return new Builder<>(serviceClass);
    }

    public static class Builder<S> {
        private final Class<S> serviceClass;
        private Predicate<Class<? extends S>> predicate = cls -> true;
        private Consumer<ScopedBindingBuilder> scopeBuilder = builder -> {};
        private final Collection<ServiceBinder.Listener<S>> listeners = new ArrayList<>();

        public Builder(Class<S> serviceClass) {
            this.serviceClass = serviceClass;
        }

        public Builder<S> apply(Consumer<Builder<S>> config) {
            config.accept(this);
            return this;
        }

        public Builder<S> filter(Predicate<Class<? extends S>> predicate) {
            this.predicate = this.predicate.and(predicate);
            return this;
        }

        public Builder<S> onBind(ServiceBinder.Listener<S> listener) {
            listeners.add(listener);
            return this;
        }

        public Builder<S> inScope(Scope scope) {
            this.scopeBuilder = scopeBuilder.andThen(b -> b.in(scope));
            return this;
        }

        public Builder<S> inScope(Class<? extends Annotation> scope) {
            this.scopeBuilder = scopeBuilder.andThen(b -> b.in(scope));
            return this;
        }

        public Module build(ServiceBinder.Factory<S> serviceBinderFactory) {
            return new AbstractModule() {
                @Override
                protected void configure() {
                    ServiceBinder<S> serviceBinder = serviceBinderFactory.createBinder(serviceClass, binder());
                    readServices(serviceClass)
                            .filter(predicate)
                            .forEach(svc -> {
                                scopeBuilder.accept(serviceBinder.bind(svc));
                                listeners.forEach(l -> l.onBind(binder(), svc));
                            });
                }
            };
        }
    }

    interface ServiceBinder<S> {
        interface Factory<S> {
            ServiceBinder<S> createBinder(Class<S> serviceClass, Binder binder);
        }

        interface Listener<S> {
            void onBind(Binder binder, Class<? extends S> serviceClass);
        }

        ScopedBindingBuilder bind(Class<? extends S> serviceImpl);
    }

    public static <S> ServiceBinder.Factory<S> toServiceSet() {
        return (serviceClass, binder) -> {
            Multibinder<S> serviceBinder = Multibinder.newSetBinder(binder, serviceClass);
            return svc -> serviceBinder.addBinding().to(svc);
        };
    }

    public static <S, K> ServiceBinder.Factory<S> toServiceMap(Class<K> keyClass, Function<Class<? extends S>, K> keyProvider) {
        return (serviceClass, binder) -> {
            MapBinder<K, S> mapBinder = MapBinder.newMapBinder(binder, keyClass, serviceClass);
            return svc -> mapBinder.addBinding(keyProvider.apply(svc)).to(svc);
        };
    }

    public static <S> ServiceBinder.Factory<S> toNamedServiceMap(Function<Class<? extends S>, String> nameProvider) {
        ServiceBinder.Factory<S> bindingFactory = toServiceMap(String.class, nameProvider);
        return (serviceClass, binder) -> {
            ServiceBinder<S> mapServiceBinder = bindingFactory.createBinder(serviceClass, binder);
            return svc -> {
                mapServiceBinder.bind(svc);
                binder.bind(serviceClass).annotatedWith(Names.named(nameProvider.apply(svc))).to(svc);
                return binder.bind(svc);
            };
        };
    }

    public static <S> ServiceBinder.Factory<S> toNamedServiceMap() {
        return toNamedServiceMap(ServiceModules::nameFromAnnotation);
    }

    public static <S, F> ServiceBinder.Factory<S> toNamedServiceFactoryMap(Class<F> factoryClass,
                                                                           Function<Class<? extends S>, String> nameProvider) {
        return (serviceClass, binder) -> {
            MapBinder<String, F> factoryMultibinging = MapBinder.newMapBinder(binder, String.class, factoryClass);
            return svc -> {
                String name = nameProvider.apply(svc);
                Key<F> factoryKey = Key.get(factoryClass, Names.named(name));
                Module module = new FactoryModuleBuilder()
                        .implement(serviceClass, svc)
                        .build(factoryKey);
                binder.install(module);
                return factoryMultibinging.addBinding(name).to(factoryKey);
            };
        };
    }

    public static <S, F> ServiceBinder.Factory<S> toNamedServiceFactoryMap(Class<F> factoryClass) {
        return toNamedServiceFactoryMap(factoryClass, ServiceModules::nameFromAnnotation);
    }

    public static <S> Map<String, Class<? extends S>> readNamedServiceMap(Class<S> serviceClass) {
        return readServices(serviceClass)
                    .collect(Collectors.toMap(ServiceModules::nameFromAnnotation, cls -> cls));
    }

    public static <S> Stream<Class<? extends S>> readServices(Class<S> serviceClass) {
        String resourcePath = "META-INF/services/" + serviceClass.getName();
        return readServices(resourcePath);
    }

    static <S> Stream<Class<? extends S>> readServices(String resourcePath) {
        return readLines(resourcePath)
                .<Class<? extends S>>map(ServiceModules::safeClassByName)
                .filter(Objects::nonNull);
    }

    static Stream<String> readLines(String resourcePath) {
        ClassLoader classLoader = ServiceModules.class.getClassLoader();
        try {
            return Streams
                    .fromEnumeration(classLoader.getResources(resourcePath))
                    .flatMap(ServiceModules::readLines);
        } catch (IOException e) {
            return Stream.empty();
        }
    }

    private static Stream<String> readLines(URL url) {
        try {
            InputStream stream = url.openStream();
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.lines()
                    .filter(Objects::nonNull)
                    .onClose(() -> {
                        try {
                            bufferedReader.close();
                        } catch (IOException ignored) {
                        }
                    });
        } catch (IOException e) {
            return Stream.empty();
        }
    }

    @SuppressWarnings("unchecked")
    private static <S> Class<? extends S> safeClassByName(String className) {
        try {
            return (Class<? extends S>)Class.forName(className);
        } catch (ClassNotFoundException e) {
            LOG.error("Cannot find service class {}", className, e);
            return null;
        }
    }

    public static String nameFromAnnotation(Class<?> cls) {
        return Optional
                .ofNullable(cls.getAnnotation(Named.class))
                .map(Named::value)
                .orElseGet(() -> Optional
                        .ofNullable(cls.getAnnotation(com.google.inject.name.Named.class))
                        .map(com.google.inject.name.Named::value)
                        .orElseGet(cls::getSimpleName));
    }
}
