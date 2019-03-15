package com.slimgears.util.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@RunWith(AnnotationRulesJUnit.class)
@UseLogLevel(UseLogLevel.Level.INFO)
public class UseLogLevelTest {
    @Test
    @UseLogLevel(value = UseLogLevel.Level.FINEST)
    @UseLogLevels({
            @UseLogLevel(value = UseLogLevel.Level.FINE, logger = "com.slimgears.util.test.UseLogLevelTest")
    })
    public void testFineOutput() {
        Set<String> receivedLevels = new HashSet<>();

        Logger.getLogger("").addHandler(new Handler() {
            @Override
            public void publish(LogRecord record) {
                receivedLevels.add(record.getLevel().getName());
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() throws SecurityException {
            }
        });

        String loggerName = UseLogLevelTest.class.getName();
        Logger.getLogger(loggerName).fine(loggerName + ": Test output");
        Logger.getLogger(loggerName).finest(loggerName + ": Test output");
        Assert.assertTrue(receivedLevels.contains("FINE"));
        Assert.assertFalse(receivedLevels.contains("FINEST"));
    }
}
