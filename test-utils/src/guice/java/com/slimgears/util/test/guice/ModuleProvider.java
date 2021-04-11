package com.slimgears.util.test.guice;

import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.slimgears.util.test.TestReflectUtils;
import org.junit.runners.model.FrameworkMethod;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

public interface ModuleProvider<A extends Annotation> {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.ANNOTATION_TYPE)
    @interface Qualifier {
        Class<? extends ModuleProvider<? extends Annotation>> value();
    }

    Module createModule(A annotation, Method testMethod, Object target);

    static Module forMethod(Method testMethod, Object target) {
        return TestReflectUtils
                .providersForMethod(testMethod, ModuleProvider.Qualifier.class, item -> {
                    Class<? extends ModuleProvider<? extends Annotation>> moduleProviderClass = item.qualifier().value();
                    ModuleProvider moduleProvider = moduleProviderClass.newInstance();
                    //noinspection unchecked
                    return moduleProvider.createModule(item.annotation(), testMethod, target);
                })
                .reduce((m1, m2) -> Modules.override(m1).with(m2))
                .orElse(Modules.EMPTY_MODULE);
    }
}
