package com.slimgears.util.guice;

import com.slimgears.util.stream.Streams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;
import java.util.stream.Stream;

public class MetaInfoResources {
    public static Stream<String> readLines(String resourcePath) {
        ClassLoader classLoader = MetaInfoResources.class.getClassLoader();
        try {
            return Streams
                .fromEnumeration(classLoader.getResources(resourcePath))
                .flatMap(MetaInfoResources::readLines);
        } catch (IOException e) {
            return Stream.empty();
        }
    }

    private static Stream<String> readLines(URL url) {
        try {
            InputStream stream = url.openStream();
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.lines()
                .filter(Objects::nonNull)
                .onClose(() -> {
                    try {
                        bufferedReader.close();
                    } catch (IOException ignored) {
                    }
                });
        } catch (IOException e) {
            return Stream.empty();
        }
    }
}
