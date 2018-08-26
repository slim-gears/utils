package com.slimgears.apt.data;

import com.google.auto.value.AutoValue;
import com.slimgears.apt.util.TypeFilters;
import com.slimgears.util.guice.ConfigProvider;
import com.slimgears.util.guice.ConfigProviders;
import com.slimgears.util.stream.Safe;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Predicate;

@AutoValue
public abstract class Environment {
    private final static String configOptionName = "rxrpc.config";
    private final static String excludedTypesOptionName = "rxrpc.excludeTypes";
    private final static String includeTypesOptionName = "rxrpc.includeTypes";
    private final static ThreadLocal<Environment> instance = new ThreadLocal<>();

    public abstract ProcessingEnvironment processingEnvironment();
    public abstract RoundEnvironment roundEnvironment();
    public abstract Properties properties();
    protected abstract Predicate<TypeInfo> ignoredTypePredicate();

    public boolean isIgnoredType(TypeInfo typeInfo) {
        return ignoredTypePredicate().test(typeInfo);
    }

    public Messager messager() {
        return processingEnvironment().getMessager();
    }

    public Types types() {
        return processingEnvironment().getTypeUtils();
    }

    public Elements elements() {
        return processingEnvironment().getElementUtils();
    }

    private static Environment create(ProcessingEnvironment processingEnvironment, RoundEnvironment roundEnvironment) {
        Properties properties = ConfigProviders.create(
                ConfigProviders.loadFromResource("/rxrpc-apt.properties"),
                ConfigProviders.fromServiceLoader(),
                loadFromExternalConfig(processingEnvironment),
                loadFromOptions(processingEnvironment));

        Predicate<TypeInfo> ignoredTypesFilter = TypeFilters
                .fromIncludedExcludedWildcard(
                        properties.getProperty(includeTypesOptionName),
                        properties.getProperty(excludedTypesOptionName))
                .negate();

        return new AutoValue_Environment(
                processingEnvironment,
                roundEnvironment,
                properties,
                ignoredTypesFilter);
    }

    public static Environment instance() {
        return Optional.ofNullable(instance.get()).orElseThrow(() -> new RuntimeException("Environment was not set"));
    }

    public static Safe.Closable withEnvironment(ProcessingEnvironment processingEnvironment, RoundEnvironment roundEnvironment) {
        Environment prev = instance.get();
        instance.set(create(processingEnvironment, roundEnvironment));
        return () -> instance.set(prev);

    }

    private static ConfigProvider loadFromExternalConfig(ProcessingEnvironment processingEnvironment) {
        return Optional
                .ofNullable(processingEnvironment.getOptions().get(configOptionName))
                .map(ConfigProviders::loadFromFile)
                .orElse(ConfigProviders.empty);
    }

    private static ConfigProvider loadFromOptions(ProcessingEnvironment processingEnvironment) {
        return props -> processingEnvironment
                .getOptions()
                .forEach((key, value) -> props.put(key, Optional
                        .ofNullable(value)
                        .map(Object::toString)
                        .orElse("true")));
    }
}
