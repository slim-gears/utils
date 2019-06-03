package com.slimgears.util.test;

import com.slimgears.util.test.logging.LogLevel;
import com.slimgears.util.test.logging.UseLogLevel;
import com.slimgears.util.test.logging.UseLogLevels;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@RunWith(AnnotationRulesJUnit.class)
@UseLogLevel(LogLevel.INFO)
public class UseLogLevelTest {
    @Test
    @UseLogLevel(value = LogLevel.DEBUG)
    @UseLogLevels({
            @UseLogLevel(value = LogLevel.DEBUG, logger = "com.slimgears.util.test.UseLogLevelTest")
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
