package com.slimgears.util.guice;

import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.slimgears.util.test.logging.LogLevel;
import com.slimgears.util.test.logging.UseLogLevel;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class AutoBindingTest {
    static class DummyModule extends AutoBindingModule {}

    @AutoBinding(module = DummyModule.class, value = Dummy.InterfaceA.class)
    private static class AutoBindingToDummyModuleClass implements Dummy.InterfaceA {

    }

    @Test @UseLogLevel(LogLevel.DEBUG)
    public void testAutoBindingWithModule() {
        Injector injector = Guice.createInjector(new DummyModule());
        assertThat(injector, hasBindingFor(Dummy.InterfaceA.class, AutoBindingToDummyModuleClass.class));
    }

    @Test
    public void testAutoBindingWithoutExclusions() {
        Injector injector = Guice.createInjector(AutoBindingModule.create());
        Dummy.InterfaceA instanceA = injector.getInstance(Dummy.InterfaceA.class);
        Dummy.InterfaceB instanceB = injector.getInstance(Dummy.InterfaceB.class);
        assertThat(instanceA, instanceOf(Dummy.InterfaceA.class));
        assertThat(instanceB, instanceOf(Dummy.InterfaceB.class));
        assertThat(instanceA, instanceOf(AutoBindingClass.class));
        assertThat(instanceB, sameInstance(instanceA));
        assertThat(injector, hasNoBindingFor(Dummy.InterfaceC.class));
    }

    @Test
    public void testAutoBindingWithExcludedInterface() {
        Injector injector = Guice.createInjector(AutoBindingModule
                .create()
                .exclude(Dummy.InterfaceA.class));

        Dummy.InterfaceB instanceB = injector.getInstance(Dummy.InterfaceB.class);
        assertThat(instanceB, instanceOf(Dummy.InterfaceB.class));

        assertThat(injector, hasNoBindingFor(Dummy.InterfaceA.class));
    }

    @Test
    public void testAutoBindingWithExcludedClass() {
        Injector injector = Guice.createInjector(AutoBindingModule
                .create()
                .exclude(AutoBindingClass.class));

        assertThat(injector, hasNoBindingFor(Dummy.InterfaceB.class));
    }

    private <I, T extends I> Matcher<Injector> hasBindingFor(Class<I> interfaceClass, Class<T> expectedImplementation) {
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

    private Matcher<Injector> hasNoBindingFor(Class<?> cls) {
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
