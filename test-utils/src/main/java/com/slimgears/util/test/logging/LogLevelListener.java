package com.slimgears.util.test.logging;

import com.slimgears.util.stream.Streams;

import java.util.ServiceLoader;

public interface LogLevelListener {
    interface Rollback {
        void apply();

        default Rollback andThen(Rollback next) {
            return () -> {
                apply();
                next.apply();
            };
        }

        static Rollback empty() {
            return () -> {};
        }
    }

    Rollback onSetLogLevel(String loggerName, LogLevel logLevel);

    static Rollback apply(String loggerName, LogLevel logLevel) {
        return Streams.fromIterable(ServiceLoader
                .load(LogLevelListener.class, LogLevelListener.class.getClassLoader()))
                .map(l -> l.onSetLogLevel(loggerName, logLevel))
                .reduce(Rollback::andThen)
                .orElseGet(Rollback::empty);
    }
}
