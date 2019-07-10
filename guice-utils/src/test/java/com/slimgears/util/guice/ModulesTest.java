package com.slimgears.util.guice;

import com.google.inject.Injector;
import org.junit.Test;

import static com.slimgears.util.guice.InjectorMatchers.hasBindingFor;
import static com.slimgears.util.guice.InjectorMatchers.hasNoBindingFor;
import static org.junit.Assert.assertThat;

public class ModulesTest {
    @Test
    public void testModuleBuilder() {
        Injector injector = Modules.injectorBuilder()
                .apply(binder -> binder.bind(Dummy.InterfaceA.class).toInstance(new AutoBindingClass()))
                .apply(binder -> binder.bind(Dummy.InterfaceC.class).toInstance(new AutoBindingClass()))
                .build();

        assertThat(injector, hasBindingFor(Dummy.InterfaceA.class, AutoBindingClass.class));
        assertThat(injector, hasBindingFor(Dummy.InterfaceC.class, AutoBindingClass.class));
        assertThat(injector, hasNoBindingFor(Dummy.InterfaceB.class));
    }
}
