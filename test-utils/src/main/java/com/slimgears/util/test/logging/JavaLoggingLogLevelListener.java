package com.slimgears.util.test.logging;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableMap;

import java.util.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

@AutoService(LogLevelListener.class)
public class JavaLoggingLogLevelListener implements LogLevelListener {
    private final static Map<LogLevel, Level> logLevelMap = ImmutableMap.<LogLevel, Level>builder()
            .put(LogLevel.ERROR, Level.SEVERE)
            .put(LogLevel.WARNING, Level.WARNING)
            .put(LogLevel.INFO, Level.INFO)
            .put(LogLevel.DEBUG, Level.FINE)
            .put(LogLevel.TRACE, Level.FINEST)
            .build();

    @Override
    public Rollback onSetLogLevel(String loggerName, LogLevel logLevel) {
        Logger logger = Logger.getLogger(loggerName);
        java.util.logging.Level level = Optional.ofNullable(logLevelMap.get(logLevel)).orElse(Level.ALL);
        java.util.logging.Level prevLevel = logger.getLevel();
        Map<Handler, Level> handlerLevelMap = new LinkedHashMap<>();
        logger.setLevel(level);
        Arrays.asList(logger.getHandlers()).forEach(h -> {
            handlerLevelMap.put(h, h.getLevel());
            h.setLevel(level);
        });

        return () -> {
            handlerLevelMap.forEach(Handler::setLevel);
            logger.setLevel(prevLevel);
        };
    }
}
