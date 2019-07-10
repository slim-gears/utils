package com.slimgears.util.guice;

import com.google.inject.*;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class Modules {
    public static class ModuleBuilder {
        private final AtomicReference<Consumer<Binder>> config = new AtomicReference<>(binder -> {});

        private ModuleBuilder() {

        }

        public ModuleBuilder apply(Consumer<Binder> config) {
            this.config.updateAndGet(c -> c.andThen(config));
            return this;
        }

        public Module build() {
            return new AbstractModule() {
                @Override
                protected void configure() {
                    ModuleBuilder.this.config.get().accept(binder());
                }
            };
        }
    }

    public static class InjectorBuilder {
        private final Injector parent;
        private final ModuleBuilder moduleBuilder = new ModuleBuilder();

        private InjectorBuilder(Injector parent) {
            this.parent = parent;
        }

        public InjectorBuilder apply(Consumer<Binder> config) {
            moduleBuilder.apply(config);
            return this;
        }

        public Injector build() {
            Module module = moduleBuilder.build();
            return Optional.ofNullable(parent)
                    .map(p -> p.createChildInjector(module))
                    .orElseGet(() -> Guice.createInjector(module));
        }
    }

    public static ModuleBuilder moduleBuilder() {
        return new ModuleBuilder();
    }

    @SuppressWarnings("WeakerAccess")
    public static InjectorBuilder injectorBuilder(Injector parent) {
        return new InjectorBuilder(parent);
    }

    public static InjectorBuilder injectorBuilder() {
        return injectorBuilder(null);
    }
}
