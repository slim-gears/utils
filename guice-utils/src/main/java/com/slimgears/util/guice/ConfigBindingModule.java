package com.slimgears.util.guice;

import com.google.common.collect.ImmutableMap;
import com.google.inject.*;
import com.google.inject.name.Names;
import com.slimgears.util.reflect.internal.Classes;
import com.slimgears.util.stream.Safe;
import com.slimgears.util.stream.Streams;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class ConfigBindingModule extends AbstractModule {
    private final static Pattern propertyNamePattern = Pattern.compile("(get|is)(?<name>[A-Z]\\w*)");
    private final static Pattern linePattern = Pattern.compile("(?<type>\\w+([.$]\\w+)*)\\s*=\\s*(?<path>\\w+(\\.\\w+)*)");
    private final static ImmutableMap<Class, Object> defaultValues = ImmutableMap.<Class, Object>builder()
            .put(int.class, 0)
            .put(long.class, 0L)
            .put(short.class, (short) 0)
            .put(double.class, 0.0)
            .put(float.class, 0.0f)
            .put(boolean.class, false)
            .put(byte.class, (byte)0)
            .put(char.class, (char)0)
            .build();
    public static final String resourcePath = "META-INF/config-map";

    @Override
    protected void configure() {
        Provider<Injector> injectorProvider = getProvider(Injector.class);
        ServiceModules.readLines(resourcePath)
                .forEach(line -> bindLine(injectorProvider, line));
    }

    @SuppressWarnings("unchecked")
    private void bindLine(Provider<Injector> injectorProvider, String line) {
        Matcher matcher = linePattern.matcher(line);
        if (matcher.matches()) {
            String type = matcher.group("type");
            String path = matcher.group("path");
            Class cls = Safe.ofCallable(() -> Class.forName(type)).get();
            bind(cls).toProvider(provider(injectorProvider, path, cls)).in(Singleton.class);
        }
    }

    private <T> T createProxy(Provider<Injector> injectorProvider, String path, Class<T> cls) {
        return createProxy(cls, new Invocator(injectorProvider, path, cls, false));
    }

    private <T> T createEagerProxy(Provider<Injector> injectorProvider, String path, Class<T> cls) {
        try {
            return createProxy(cls, new Invocator(injectorProvider, path, cls, true));
        } catch (ConfigurationException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T createProxy(Class<T> cls, InvocationHandler invocationHandler) {
        return (T)Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { cls },
                invocationHandler);
    }

    class Invocator implements InvocationHandler {
        private final Provider<Injector> injectorProvider;
        private final String path;
        private final Map<String, Object> values = new ConcurrentHashMap<>();

        Invocator(Provider<Injector> injectorProvider, String path, Class<?> clazz, boolean eager) {
            this.injectorProvider = injectorProvider;
            this.path = path;
            if (eager) {
                Classes.allMethods(clazz)
                        .filter(m -> m.getDeclaringClass() != Object.class)
                        .forEach(m -> {
                            String propertyName = toPropertyName(m);
                            values.put(propertyName, retrieveValue(propertyName, m.getReturnType()));
                        });
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(this, args);
            }

            Class<?> type = method.getReturnType();

            if (method.getParameterCount() != 0 || type == void.class || type == Void.class) {
                throw new RuntimeException("Configuration interface should have only getter methods");
            }

            return values.computeIfAbsent(toPropertyName(method), name -> retrieveValue(name, type));
        }

        private Object retrieveValue(String name, Class<?> type) {
            String propertyPath = path + "." + name;
            if (type.isArray() && type.getComponentType().isInterface()) {
                Class<?> componentType = type.getComponentType();
                AtomicInteger integer = new AtomicInteger();
                return Streams.takeWhile(IntStream
                        .generate(integer::getAndIncrement).mapToObj(i -> propertyPath + "." + i)
                        .map(p -> createEagerProxy(injectorProvider, p, componentType)),
                        Objects::nonNull)
                        .toArray(size -> (Object[])Array.newInstance(componentType, size));
            }

            return Optional
                    .<Object>ofNullable(provider(injectorProvider, propertyPath, type).get())
                    .orElseGet(() -> type.isPrimitive() ? defaultValues.get(type) : null);
        }

        @Override
        public String toString() {
            return "Configuration(path: " + path + ")";
        }
    }

    private String toPropertyName(Method method) {
        Matcher matcher = propertyNamePattern.matcher(method.getName());
        return matcher.matches()
                ? toPropertyName(matcher.group("name"))
                : method.getName();
    }

    private String toPropertyName(String name) {
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }

    private <T> Provider<T> provider(Provider<Injector> injectorProvider, String path, Class<T> cls) {
        return cls.isInterface()
                ? () -> createProxy(injectorProvider, path, cls)
                : () -> injectorProvider.get().getInstance(Key.get(cls, Names.named(path)));
    }
}
