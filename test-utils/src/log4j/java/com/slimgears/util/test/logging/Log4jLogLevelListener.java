package com.slimgears.util.test.logging;

import com.google.auto.service.AutoService;
import com.google.common.base.Strings;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.util.Optional;

@AutoService(LogLevelListener.class)
public class Log4jLogLevelListener implements LogLevelListener {
    @Override
    public Rollback onSetLogLevel(String loggerName, LogLevel logLevel) {
        return Optional
                .of(setLogLevel(loggerName, logLevel))
                .orElseGet(Rollback::empty);
    }

    private Rollback setLogLevel(String loggerName, LogLevel level) {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(Strings.isNullOrEmpty(loggerName) ? LogManager.ROOT_LOGGER_NAME : loggerName);
        Level newLevel = Level.toLevel(level.name());
        Level prevLevel = loggerConfig.getLevel();
        loggerConfig.setLevel(newLevel);
        context.updateLoggers();
        return () -> {
            loggerConfig.setLevel(prevLevel);
            context.updateLoggers();
        };
    }
}
