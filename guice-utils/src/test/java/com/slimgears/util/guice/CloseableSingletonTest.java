package com.slimgears.util.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class CloseableSingletonTest {
    @CloseableSingleton
    static class DummyCloseableClass implements AutoCloseable {
        private final AtomicBoolean closed;

        @Inject
        public DummyCloseableClass(AtomicBoolean closed) {
            this.closed = closed;
        }

        public boolean isClosed() {
            return this.closed.get();
        }

        @Override
        public void close() throws Exception {
            closed.set(true);
        }
    }

    @CloseableSingleton
    static class DummyNonCloseableClass {

    }

    @Test
    public void testCloseableSingleton() {
        AtomicBoolean wasClosed = new AtomicBoolean();
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                CloseableSingletonScope.install(binder());
                bind(AtomicBoolean.class).toInstance(wasClosed);
            }
        });

        DummyCloseableClass dummyCloseableClass = injector.getInstance(DummyCloseableClass.class);
        DummyNonCloseableClass dummyNonCloseableClass = injector.getInstance(DummyNonCloseableClass.class);
        CloseableSingletonScope closeableSingletonScope = injector.getInstance(CloseableSingletonScope.class);

        Assert.assertSame(dummyNonCloseableClass, injector.getInstance(DummyNonCloseableClass.class));
        Assert.assertSame(dummyCloseableClass, injector.getInstance(DummyCloseableClass.class));

        Assert.assertFalse(dummyCloseableClass.isClosed());
        closeableSingletonScope.close();

        Assert.assertTrue(wasClosed.get());
        Assert.assertTrue(dummyCloseableClass.isClosed());

    }
}
