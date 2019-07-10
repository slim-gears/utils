package com.slimgears.util.guice;

import com.google.common.collect.ImmutableMap;
import com.google.inject.*;
import com.google.inject.name.Names;
import com.slimgears.util.stream.Optionals;
import com.slimgears.util.stream.Safe;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            bind(cls).toProvider(provider(injectorProvider, path, cls));
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T createProxy(Provider<Injector> injectorProvider, String path, Class<T> cls) {
        return (T)Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { cls },
                new Invocator(injectorProvider, path));
    }

    class Invocator implements InvocationHandler {
        private final Provider<Injector> injectorProvider;
        private final String path;
        private final Map<String, Object> values = new ConcurrentHashMap<>();

        Invocator(Provider<Injector> injectorProvider, String path) {
            this.injectorProvider = injectorProvider;
            this.path = path;
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

            String propertyName = toPropertyName(method);
            return values.computeIfAbsent(propertyName, name -> {
                String propertyPath = path + "." + propertyName;
                return Optional
                        .<Object>ofNullable(provider(injectorProvider, propertyPath, type).get())
                        .orElseGet(() -> type.isPrimitive() ? defaultValues.get(type) : null);
            });
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
