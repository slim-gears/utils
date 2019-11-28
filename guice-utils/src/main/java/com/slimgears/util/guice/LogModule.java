package com.slimgears.util.guice;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogModule extends AbstractModule {
    @SuppressWarnings("Convert2MethodRef")
    @Override
    protected void configure() {
        FieldInjector
                .inject(Logger.class)
                .toAnnotatedField(InjectLog.class)
                .resolveByClass((Class<?> cls) -> LoggerFactory.getLogger(cls))
                .install(binder());
    }
}
