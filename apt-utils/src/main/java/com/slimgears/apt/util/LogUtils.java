/**
 *
 */
package com.slimgears.apt.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AppenderBase;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import java.util.Optional;

public class LogUtils {
    private final static Logger log = LoggerFactory.getLogger(LogUtils.class);

    public static SelfClosable applyLogging(ProcessingEnvironment environment) {
        setVerbosity(environment.getOptions().get("verbosity"));
        return MessagerAppender.install(environment.getMessager());
    }

    public interface SelfClosable extends AutoCloseable {
        void close();
    }

    public static void dumpContent(String content) {
        if (!log.isTraceEnabled()) {
            return;
        }

        log.trace("\n" + content);
    }

    private static void setVerbosity(String verbosity) {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getILoggerFactory().getLogger(Logger.ROOT_LOGGER_NAME);
        Level logLevel = Optional
                .ofNullable(verbosity)
                .map(Level::toLevel)
                .orElse(Level.ERROR);

        logger.setLevel(logLevel);
    }

    private static class MessagerAppender extends AppenderBase<ILoggingEvent> {
        private final static ImmutableMap<Level, Diagnostic.Kind> logLevelMap = ImmutableMap.<Level, Diagnostic.Kind>builder()
                .put(Level.TRACE, Diagnostic.Kind.OTHER)
                .put(Level.DEBUG, Diagnostic.Kind.OTHER)
                .put(Level.INFO, Diagnostic.Kind.NOTE)
                .put(Level.WARN, Diagnostic.Kind.WARNING)
                .put(Level.ERROR, Diagnostic.Kind.ERROR)
                .build();

        private final Messager messager;

        public static SelfClosable install(Messager messager) {
            LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();
            ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);

            Appender<ILoggingEvent> appender = new MessagerAppender(messager);
            appender.setContext(loggerContext);
            appender.setName("Messager");
            appender.start();

            logger.addAppender(appender);

            return () -> {
                appender.stop();
                logger.detachAppender(appender);
            };
        }

        private MessagerAppender(Messager messager) {
            this.messager = messager;
        }

        @Override
        protected void append(ILoggingEvent eventObject) {
            Diagnostic.Kind kind = logLevelMap.get(eventObject.getLevel());
            this.messager.printMessage(kind, eventObject.getFormattedMessage());
        }
    }
}
