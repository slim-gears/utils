/**
 *
 */
package com.slimgears.util.generic;

import com.slimgears.util.reflect.TypeToken;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ServiceResolvers {
    public final static ServiceResolver defaultResolver = defaultConstructorResolver();

    public static ServiceResolver defaultConstructorResolver() {
        return new ServiceResolver() {
            @Override
            public <T> T resolve(TypeToken<T> token) {
                return canResolve(token)
                        ? token.newInstance()
                        : null;
            }

            @Override
            public boolean canResolve(TypeToken<?> token) {
                return !token.isInterface() &&
                        !token.hasModifier(Modifier::isAbstract) &&
                        token.constructor() != null;
            }
        };
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<TypeToken, Supplier> bindingMap = new HashMap<>();
        private ServiceResolver upstream = defaultResolver;

        public Builder parentResolver(ServiceResolver resolver) {
            upstream = resolver;
            return this;
        }

        public Builder apply(Consumer<Builder> config) {
            config.accept(this);
            return this;
        }

        public <T> BindingBuilder<T> bind(Class<T> cls) {
            return bind(TypeToken.of(cls));
        }
        public <T> BindingBuilder<T> bind(TypeToken<T> token) {
            return new BindingBuilder<>(token);
        }

        public ServiceResolver build() {
            return new ServiceResolver() {
                @SuppressWarnings("unchecked")
                @Override
                public <T> T resolve(TypeToken<T> cls) {
                    return Optional
                            .ofNullable(bindingMap.get(cls))
                            .map(s -> (T)s.get())
                            .orElseGet(() -> upstream.resolve(cls));
                }

                @Override
                public boolean canResolve(TypeToken<?> token) {
                    return bindingMap.containsKey(token) || upstream.canResolve(token);
                }
            };
        }

        public class BindingBuilder<T> {
            private final TypeToken<T> token;

            private BindingBuilder(TypeToken<T> token) {
                this.token = token;
            }

            public Builder toInstance(T instance) {
                return toSupplier(() -> instance);
            }

            public Builder toSupplier(Supplier<T> supplier) {
                bindingMap.put(token, supplier);
                return Builder.this;
            }

            public Builder to(TypeToken<? extends T> token) {
                return toSupplier(() -> upstream.resolve(token));
            }

            public Builder to(Class<? extends T> cls) {
                return to(TypeToken.of(cls));
            }
        }
    }
}
