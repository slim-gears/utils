package com.slimgears.apt.util;

import com.slimgears.apt.data.Environment;
import com.slimgears.util.generic.ScopedInstance;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Objects;

public class StoreWrittenFilesRule implements TestRule {
    private final File outputFolder;

    private StoreWrittenFilesRule(File outputFolder) {
        this.outputFolder = outputFolder;
        if (outputFolder.exists()) {
            Arrays.asList(Objects.requireNonNull(outputFolder.listFiles()))
                    .forEach(File::delete);
        } else {
            outputFolder.mkdirs();
        }
    }

    public String path() {
        return outputFolder.getAbsolutePath();
    }

    public static StoreWrittenFilesRule forPath(String path) {
        return new StoreWrittenFilesRule(new File(path));
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                ScopedInstance.Closeable closeable = Environment.withFileListener(StoreWrittenFilesRule.this::writeFile);
                try {
                    base.evaluate();
                } finally {
                    closeable.close();
                }
            }
        };
    }

    private void writeFile(String filename, String content) {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFolder.toPath().resolve(filename), StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
