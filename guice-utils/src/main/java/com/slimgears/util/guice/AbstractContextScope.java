package com.slimgears.util.guice;

import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.util.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

@SuppressWarnings("WeakerAccess")
abstract class AbstractContextScope<C extends AbstractContextScope.Context<C>> implements Scope {
    private final static Logger LOG = LoggerFactory.getLogger(AbstractContextScope.class);

    private final List<Provider> autoInstantiated = new ArrayList<>();
    private final Binder binder;
    private final ThreadLocal<C> currentContext = new ThreadLocal<>();
    private final Map<Key<?>, List<Consumer>> listenerMap = new ConcurrentHashMap<>();
    private final List<Listener<C>> contextListeners = new CopyOnWriteArrayList<>();
    private final Provider<Injector> injectorProvider;
    private final Class<C> contextClass;
    private final Collection<C> contexts = new CopyOnWriteArrayList<>();

    @SuppressWarnings("unchecked")
    AbstractContextScope(Binder binder, Class<C> contextClass) {
        this.binder = binder;
        this.injectorProvider = binder.getProvider(Injector.class);
        this.contextClass = contextClass;

        Key<Collection<C>> contextCollectionKey = (Key<Collection<C>>)Key.get(Types.newParameterizedType(Collection.class, contextClass));
        this.binder.bind(contextCollectionKey).toInstance(contexts);
        this.binder.bind(contextClass).toProvider(() -> null).in(this);
    }

    public interface Listener<C extends AbstractContextScope.Context<C>> {
        void onCreated(C context);
        void onClosing(C context);
    }

    @SuppressWarnings("WeakerAccess")
    public static class Context<C extends Context<C>> {
        private static int lastContextId = 0;
        private final ObjectStorage objectStorage = new ObjectStorage();
        private final AbstractContextScope<C> scope;
        private final Collection<Runnable> onCloseListeners = new ArrayList<>();
        private final int contextId;

        Context(AbstractContextScope<C> scope) {
            this.contextId = ++lastContextId;
            this.scope = scope;
            this.objectStorage.subscribe(this::onInstantiated);
            this.objectStorage.get(Key.get(scope.contextClass), this::self);
            LOG.info("Context {} created", contextId);
        }

        public <T> Provider<T> getProvider(Provider<T> provider) {
            return () -> execute(provider::get);
        }

        public <T> javax.inject.Provider<T> getProvider(javax.inject.Provider<T> provider) {
            return () -> execute(provider::get);
        }

        public <T> Provider<T> getProvider(Key<T> key) {
            return () -> getInstance(key);
        }

        public <T> Provider<T> getProvider(Class<T> cls) {
            return getProvider(Key.get(cls));
        }

        public <T> T getInstance(Key<T> key) {
            return execute(() -> scope.injectorProvider.get().getInstance(key));
        }

        public <T> T getInstance(Class<T> cls) {
            return getInstance(Key.get(cls));
        }

        <T> T getObject(Key<T> key, Provider<T> unscopedProvider) {
            return objectStorage.get(key, unscopedProvider);
        }

        public boolean contains(Key<?> key) {
            return objectStorage.contains(key);
        }

        public boolean contains(Class<?> cls) {
            return contains(Key.get(cls));
        }

        public <T> Optional<T> getExistingInstance(Key<T> key) {
            return objectStorage.contains(key) ? Optional.of(getInstance(key)) : Optional.empty();
        }

        public <T> Optional<T> getExistingInstance(Class<T> cls) {
            return getExistingInstance(Key.get(cls));
        }

        public int getId() {
            return contextId;
        }

        public void close() {
            if (scope.contexts.contains(self())) {
                scope.contextListeners.forEach(l -> l.onClosing(self()));
                objectStorage.close();
                scope.contexts.remove(self());
                onCloseListeners.forEach(Runnable::run);
                LOG.info("Context {} closed", contextId);
            }
        }

        protected C self() {
            //noinspection unchecked
            return (C)this;
        }

        protected <T> void onInstantiated(Key<T> key, T instance) {
            //noinspection unchecked
            Optional
                    .ofNullable(scope.listenerMap.get(key))
                    .map(Collection::stream)
                    .orElseGet(Stream::empty)
                    .forEach(l -> l.accept(instance));
        }

        public <T> T execute(Callable<T> callable) {
            C prev = scope.current();
            scope.currentContext.set(self());
            try {
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                scope.currentContext.set(prev);
            }
        }

        public void execute(Runnable runnable) {
            execute(() -> { runnable.run(); return null; });
        }

        public void subscribeOnClose(Runnable action) {
            onCloseListeners.add(action);
        }

        public void unsubscribeOnClose(Runnable action) {
            onCloseListeners.remove(action);
        }
    }

    public <T> void addAutoInstantiated(Class<T> cls) {
        addAutoInstantiated(Key.get(cls));
    }

    public <T> void addAutoInstantiated(Key<T> key) {
        autoInstantiated.add(binder.getProvider(key));
    }

    public void addContextListener(Listener<C> listener) {
        contextListeners.add(listener);
        for (C context : contexts) {
            listener.onCreated(context);
        }
    }

    public <T> void addInstantiationListener(Class<T> cls, Consumer<T> listener) {
        addInstantiationListener(Key.get(cls), listener);
    }

    public <T> void addInstantiationListener(Key<T> key, Consumer<T> listener) {
        Optional
                .ofNullable(listenerMap.get(key))
                .orElseGet(() -> {
                    List<Consumer> listeners = new CopyOnWriteArrayList<>();
                    listenerMap.put(key, listeners);
                    return listeners;
                })
                .add(listener);
    }

    public <T> void addInstantiationListener(Class<T> cls, BiConsumer<C, T> listener) {
        addInstantiationListener(Key.get(cls), listener);
    }

    public <T> void addInstantiationListener(Key<T> key, BiConsumer<C, T> listener) {
        Optional
                .ofNullable(listenerMap.get(key))
                .orElseGet(() -> {
                    List<Consumer> listeners = new CopyOnWriteArrayList<>();
                    listenerMap.put(key, listeners);
                    return listeners;
                })
                .add(obj -> {
                    C context = current();
                    //noinspection unchecked
                    listener.accept(context, (T)obj);
                });
    }

    public C createNew() {
        C context = createContext();
        context.execute(() -> autoInstantiated.forEach(Provider::get));
        contexts.add(context);
        contextListeners.forEach(l -> l.onCreated(context));
        return context;
    }

    public C current() {
        return currentContext.get();
    }

    public boolean hasCurrent() {
        return current() != null;
    }

    protected abstract C createContext();

    @Override
    public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped) {
        return () -> Optional
                .ofNullable(current())
                .map(c -> c.getObject(key, unscoped))
                .orElseThrow(() -> new RuntimeException("No context is currently applied"));
    }
}
