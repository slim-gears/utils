package com.slimgears.util.test.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.auto.service.AutoService;
import com.google.common.base.Strings;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.slimgears.util.stream.Optionals.ofType;

@AutoService(LogLevelListener.class)
public class LogbackLogLevelListener implements LogLevelListener {
    @Override
    public Rollback onSetLogLevel(String loggerName, LogLevel logLevel) {
        return Optional
                .ofNullable(LoggerFactory.getLogger(Strings.isNullOrEmpty(loggerName) ? Logger.ROOT_LOGGER_NAME : loggerName))
                .flatMap(ofType(Logger.class))
                .map(logger -> setLogLevel(logger, logLevel))
                .orElseGet(Rollback::empty);
    }

    private Rollback setLogLevel(Logger logger, LogLevel level) {
        Level newLevel = Level.toLevel(level.name());
        Level prevLevel = logger.getLevel();
        logger.setLevel(newLevel);
        return () -> logger.setLevel(prevLevel);
    }
}
