package com.slimgears.apt.data;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import com.slimgears.apt.util.TypeFilters;
import com.slimgears.util.generic.ScopedInstance;
import com.slimgears.util.guice.ConfigProvider;
import com.slimgears.util.guice.ConfigProviders;
import com.slimgears.util.stream.Safe;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Predicate;

@AutoValue
public abstract class Environment implements Safe.Closeable {
    private final ScopedInstance.Closeable closeable;
    public final static String configOptionName = "apt.config";
    public final static String excludedTypesOptionName = "apt.excludeTypes";
    public final static String includeTypesOptionName = "apt.includeTypes";
    private final static ScopedInstance<Environment> instance = ScopedInstance.create();
    private final static ScopedInstance<FileListener> fileListenerInstance = ScopedInstance.create(FileListener.empty);

    public interface FileListener {
        FileListener empty = (f, c) -> {};
        void onFileWrite(String filename, String content);

        default FileListener add(FileListener listener) {
            return (f, c) -> {
                onFileWrite(f, c);
                listener.onFileWrite(f, c);
            };
        }
    }

    public abstract ProcessingEnvironment processingEnvironment();
    public abstract RoundEnvironment roundEnvironment();
    public abstract ImmutableMap<String, String> properties();

    public FileListener fileListener() {
        return fileListenerInstance.current();
    }

    protected Environment() {
        this.closeable = instance.scope(this);
    }

    public void close() {
        closeable.close();
    }

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

    public static ScopedInstance.Closeable withFileListener(FileListener listener) {
        return fileListenerInstance.scope(fileListenerInstance.current().add(listener));
    }

    protected abstract Builder toBuilderInternal();

    public Builder toBuilder() {
        return toBuilderInternal().propertiesFrom(this.properties());
    }

    private static Environment create(ProcessingEnvironment processingEnvironment, RoundEnvironment roundEnvironment) {
        Properties properties = ConfigProviders.create(
                ConfigProviders.loadFromResource("/apt.properties"),
                ConfigProviders.fromServiceLoader(),
                loadFromExternalConfig(processingEnvironment),
                loadFromOptions(processingEnvironment));

        return builder()
                .processingEnvironment(processingEnvironment)
                .roundEnvironment(roundEnvironment)
                .ignoredTypePredicate(TypeFilters
                        .fromIncludedExcludedWildcard(
                                properties.getProperty(includeTypesOptionName),
                                properties.getProperty(excludedTypesOptionName))
                        .negate())
                .propertiesFrom(properties)
                .build();
    }

    private static Builder builder() {
        return new AutoValue_Environment.Builder();
    }

    public static Environment instance() {
        return Optional.ofNullable(instance.current()).orElseThrow(() -> new RuntimeException("Environment was not set"));
    }

    public static Safe.Closeable withEnvironment(ProcessingEnvironment processingEnvironment, RoundEnvironment roundEnvironment) {
        return create(processingEnvironment, roundEnvironment);
    }

    private static ConfigProvider loadFromExternalConfig(ProcessingEnvironment processingEnvironment) {
        return Optional
                .ofNullable(processingEnvironment.getOptions().get(configOptionName))
                .map(ConfigProviders::loadFromFile)
                .orElse(ConfigProviders.empty);
    }

    private static ConfigProvider loadFromOptions(ProcessingEnvironment processingEnvironment) {
        return loadFromOptions(processingEnvironment.getOptions());
    }

    private static ConfigProvider loadFromOptions(Map<String, ?> options) {
        return props -> options
                .forEach((key, value) -> props.put(key, Optional
                        .ofNullable(value)
                        .map(Object::toString)
                        .orElse("true")));
    }

    @SuppressWarnings("WeakerAccess")
    @AutoValue.Builder
    public static abstract class Builder {
        private final Map<String, String> properties = new HashMap<>();

        public abstract Builder processingEnvironment(ProcessingEnvironment processingEnvironment);
        public abstract Builder roundEnvironment(RoundEnvironment roundEnvironment);
        public abstract Builder ignoredTypePredicate(Predicate<TypeInfo> filter);
        protected abstract Builder properties(ImmutableMap<String, String> properties);
        protected abstract Environment buildInternal();

        public Environment build() {
            properties(ImmutableMap.copyOf(properties));
            return buildInternal();
        }

        public Builder propertiesFrom(Map<?, ?> options) {
            options.forEach((key, value) -> properties.put(key.toString(), value.toString()));
            return this;
        }

        public Builder propertiesFrom(ConfigProvider... providers) {
            return propertiesFrom(ConfigProviders.create(providers));
        }
    }
}
