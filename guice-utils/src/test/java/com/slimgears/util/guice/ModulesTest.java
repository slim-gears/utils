package com.slimgears.util.guice;

import com.google.inject.Injector;
import org.junit.Test;

import javax.inject.Inject;

import static com.slimgears.util.guice.InjectorMatchers.hasBindingFor;
import static com.slimgears.util.guice.InjectorMatchers.hasNoBindingFor;
import static org.junit.Assert.assertThat;

public class ModulesTest {
    static class DependsOnParentInjector implements Dummy.InterfaceC {
        private final Dummy.InterfaceA interfaceA;

        @Inject
        public DependsOnParentInjector(Dummy.InterfaceA interfaceA) {
            this.interfaceA = interfaceA;
        }
    }

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

    @Test
    public void testChildInjectorBuilder() {
        Injector parentInjector = Modules.injectorBuilder()
                .apply(b -> b.bind(Dummy.InterfaceA.class).to(AutoBindingClass.class))
                .build();

        Injector childInjector = Modules.injectorBuilder(parentInjector)
                .apply(b -> b.bind(Dummy.InterfaceC.class).to(DependsOnParentInjector.class))
                .build();

        assertThat(childInjector, hasBindingFor(Dummy.InterfaceC.class, DependsOnParentInjector.class));
    }
}
