package com.slimgears.util.guice;

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.instanceOf;

class InjectorMatchers {
    static <I, T extends I> Matcher<Injector> hasBindingFor(Class<I> interfaceClass, Class<T> expectedImplementation) {
        return new BaseMatcher<Injector>() {
            @Override
            public boolean matches(Object item) {
                I instance;
                try {
                    Injector injector = (Injector)item;
                    instance = injector.getInstance(interfaceClass);
                } catch (Throwable ignored) {
                    return false;
                }
                return instanceOf(expectedImplementation).matches(instance);
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue(interfaceClass.getName());
                description.appendText(" binding not found");
            }
        };
    }

    static Matcher<Injector> hasNoBindingFor(Class<?> cls) {
        return new BaseMatcher<Injector>() {
            @Override
            public boolean matches(Object item) {
                try {
                    Injector injector = (Injector)item;
                    injector.getInstance(cls);
                    return false;
                } catch (ConfigurationException e) {
                    return true;
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue(cls.getName());
                description.appendText(" was not excluded");
            }
        };
    }
}
