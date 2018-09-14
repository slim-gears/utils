/**
 *
 */
package com.slimgears.util.guice;

import com.google.inject.*;
import com.google.inject.assistedinject.FactoryModuleBuilder;
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
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServiceModules {
    private final static Logger LOG = LoggerFactory.getLogger(ServiceModules.class);

    public static <S> Module forServiceSet(Class<S> serviceClass) {
        return new ServiceSetModule<>(serviceClass);
    }

    public static <S> ServiceMapModule<String, S> forNamedServiceMap(Class<S> serviceClass) {
        return new AnnotatedNamedServiceMapModule<>(serviceClass);
    }

    public static <S, F> Module forNamedServiceFactoryMap(Class<S> serviceClass, Class<F> serviceFactoryClass) {
        return new AnnotatedNamedServiceFactoryMapModule<>(serviceClass, serviceFactoryClass);
    }

    public static <K, S> Module forServiceMap(Class<K> keyClass, Class<S> serviceClass, Function<Class<? extends S>, K> keyGetter) {
        return new ServiceMapModule<>(
                keyClass,
                serviceClass,
                keyGetter);
    }

    static class ServiceSetModule<S> extends AbstractModule {
        private final Class<S> serviceClass;

        ServiceSetModule(Class<S> serviceClass) {
            this.serviceClass = serviceClass;
        }

        @Override
        protected void configure() {
            Multibinder<S> serviceBinder = Multibinder.newSetBinder(binder(), serviceClass);
            Stream<Class<? extends S>> serviceClasses = readServices(serviceClass);
            serviceClasses.forEach(serviceImplClass ->
                    serviceBinder.addBinding().to(serviceImplClass).in(Singleton.class));
        }
    }

    public static class AnnotatedNamedServiceMapModule<S> extends ServiceMapModule<String, S> {
        AnnotatedNamedServiceMapModule(Class<S> serviceClass) {
            super(String.class, serviceClass, ServiceModules::nameFromAnnotation);
        }

        @Override
        protected void addBinding(MapBinder<String, S> binder, String key, Class<? extends S> serviceImplClass) {
            super.addBinding(binder, key, serviceImplClass);
            bind(serviceClass).annotatedWith(Names.named(key)).to(serviceImplClass);
        }
    }

    public static class AnnotatedNamedServiceFactoryMapModule<S, F> extends AbstractModule {
        private final Class<S> serviceClass;
        private final Class<F> factoryClass;

        AnnotatedNamedServiceFactoryMapModule(Class<S> serviceClass, Class<F> factoryClass) {
            this.serviceClass = serviceClass;
            this.factoryClass = factoryClass;
        }

        @Override
        protected void configure() {
            Map<String, Class<? extends S>> stringClassMap = ServiceModules.readNamedServiceMap(serviceClass);
            MapBinder<String, F> strategyFactoryMultibinging = MapBinder.newMapBinder(binder(), String.class, factoryClass);
            stringClassMap.forEach((name, cls) -> {
                Key<F> factoryKey = Key.get(factoryClass, Names.named(name));
                Module module = new FactoryModuleBuilder()
                        .implement(serviceClass, cls)
                        .build(factoryKey);
                install(module);
                strategyFactoryMultibinging.addBinding(name).to(factoryKey);
            });
        }
    }

    public interface MapBindingListener<K, S> {
        void onBind(Binder binder, K key, Class<? extends S> serviceClass);
    }

    public static class ServiceMapModule<K, S> extends AbstractModule {
        final Class<S> serviceClass;
        private final Class<K> keyClass;
        private final Function<Class<? extends S>, K> keyProvider;
        private final Collection<MapBindingListener<K, S>> listeners = new ArrayList<>();

        ServiceMapModule(Class<K> keyClass, Class<S> serviceClass, Function<Class<? extends S>, K> keyProvider) {
            this.keyClass = keyClass;
            this.serviceClass = serviceClass;
            this.keyProvider = keyProvider;
        }

        public ServiceMapModule<K, S> onBinding(MapBindingListener<K, S> listener) {
            listeners.add(listener);
            return this;
        }

        @Override
        protected void configure() {
            MapBinder<K, S> serviceBinder = MapBinder.newMapBinder(binder(), keyClass, serviceClass);
            Stream<Class<? extends S>> serviceClasses = readServices(serviceClass);
            serviceClasses.forEach(serviceImplClass -> {
                K key = keyProvider.apply(serviceImplClass);
                addBinding(serviceBinder, keyProvider.apply(serviceImplClass), serviceImplClass);
                listeners.forEach(l -> l.onBind(binder(), key, serviceImplClass));
            });
        }

        protected void addBinding(MapBinder<K, S> binder, K key, Class<? extends S> serviceClass) {
            binder.addBinding(key).to(serviceClass);
        }
    }

    public static <S> Map<String, Class<? extends S>> readNamedServiceMap(Class<S> serviceClass) {
        return readServices(serviceClass)
                .collect(Collectors.toMap(ServiceModules::nameFromAnnotation, cls -> cls));
    }

    public static <S> Stream<Class<? extends S>> readServices(Class<S> serviceClass) {
        ClassLoader classLoader = ServiceModules.class.getClassLoader();
        String resourcePath = String.format("META-INF/services/%s", serviceClass.getName());
        try {
            return Streams
                    .fromEnumeration(classLoader.getResources(resourcePath))
                    .flatMap(ServiceModules::readServices);
        } catch (IOException e) {
            return Stream.empty();
        }
    }

    private static <S> Stream<Class<? extends S>> readServices(URL url) {
        try {
            InputStream stream = url.openStream();
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.lines()
                    .<Class<? extends S>>map(ServiceModules::safeClassByName)
                    .filter(Objects::nonNull)
                    .onClose(() -> {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                        }
                    });
        } catch (IOException e) {
            return Stream.empty();
        }
    }

    private static <S> Class<? extends S> safeClassByName(String className) {
        try {
            //noinspection unchecked
            return (Class<? extends S>)Class.forName(className);
        } catch (ClassNotFoundException e) {
            LOG.error("Cannot find service class {}", className, e);
            return null;
        }
    }

    private static String nameFromAnnotation(Class<?> cls) {
        return Optional
                .ofNullable(cls.getAnnotation(Named.class))
                .map(Named::value)
                .orElseGet(() -> Optional
                        .ofNullable(cls.getAnnotation(com.google.inject.name.Named.class))
                        .map(com.google.inject.name.Named::value)
                        .orElseGet(cls::getSimpleName));
    }
}
