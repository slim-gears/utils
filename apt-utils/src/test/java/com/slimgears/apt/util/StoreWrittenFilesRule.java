package com.slimgears.apt.util;

import com.google.common.collect.Sets;
import com.slimgears.apt.data.Environment;
import com.slimgears.util.generic.ScopedInstance;
import com.slimgears.util.stream.Streams;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class StoreWrittenFilesRule implements TestRule {
    private final File outputFolder;
    private final Set<String> createdFileNames = Sets.newHashSet();

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

    public Iterable<String> createdFileNames() {
        return createdFileNames;
    }

    public Iterable<File> createdFiles() {
        return Streams.fromIterable(createdFileNames)
                .map(name -> outputFolder.toPath().resolve(name))
                .map(Path::toFile)
                .collect(Collectors.toSet());
    }

    public boolean wasCreated(String filename) {
        return createdFileNames.contains(filename);
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
                createdFileNames.clear();
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
            createdFileNames.add(filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
