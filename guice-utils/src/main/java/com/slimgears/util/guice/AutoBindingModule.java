package com.slimgears.util.guice;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class AutoBindingModule extends AbstractModule {
    public final static String resourcePath = "META-INF/autobinding/";
    private final static Logger log = LoggerFactory.getLogger(AutoBindingModule.class);
    private final Set<Class<?>> excludedClassesOrInterfaces = new HashSet<>();

    protected AutoBindingModule() {

    }

    public static AutoBindingModule create() {
        return new AutoBindingModule();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void configure() {
        ServiceModules.readServices(resourcePath + getClass().getName())
                .peek(cls -> log.debug("Found binding: ${}", cls.getName()))
                .filter(notExcluded())
                .forEach(cls -> {
                    AutoBinding annotation = cls.getAnnotation(AutoBinding.class);
                    Arrays.stream(annotation.value())
                            .filter(notExcluded())
                            .forEach(iface -> bind((Class)iface).to(cls));
                });
    }

    private Predicate<Class<?>> notExcluded() {
        return cls -> !excludedClassesOrInterfaces.contains(cls);
    }

    public AutoBindingModule exclude(Class<?>... classesOrInterfaces) {
        excludedClassesOrInterfaces.addAll(Arrays.asList(classesOrInterfaces));
        return this;
    }
}
