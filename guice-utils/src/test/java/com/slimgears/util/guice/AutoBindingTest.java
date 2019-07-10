package com.slimgears.util.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.slimgears.util.test.logging.LogLevel;
import com.slimgears.util.test.logging.UseLogLevel;
import org.junit.Test;

import static com.slimgears.util.guice.InjectorMatchers.hasBindingFor;
import static com.slimgears.util.guice.InjectorMatchers.hasNoBindingFor;
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
}
