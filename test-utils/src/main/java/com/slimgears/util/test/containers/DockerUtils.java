package com.slimgears.util.test.containers;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.SneakyThrows;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class DockerUtils {
    private static String defaultComposeFile = "docker-compose.yaml";

    public static void start() {
        start(defaultComposeFile);
    }

    public static void stop() {
        stop(defaultComposeFile);
    }

    public static void start(String composeFilePath) {
        System.out.println("Starting container");
        try {
            execute("docker-compose", "-f", composeFilePath, "up", "-d");
            Thread.sleep(8000);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void stop(String composeFilePath) {
        System.out.println("Stopping container");
        if (System.getProperty("tearDown.keepContainer") != null) {
            return;
        }

        execute("docker-compose", "-f", composeFilePath, "down");
    }

    public static boolean isAvailable() {
        return Optional.ofNullable(execute("docker-compose", "--version"))
                .filter(out -> out.startsWith("docker-compose version"))
                .isPresent();
    }

    @SneakyThrows
    public static AutoCloseable withContainer(ContainerConfig config) {
        ImmutableList.Builder<String> cmdLineBuilder = ImmutableList.<String>builder().add("docker", "run", "-d", "--rm");
        if (!Strings.isNullOrEmpty(config.containerName())) {
            String containerName = config.containerName();
            cmdLineBuilder.add("--name", containerName);
            String id = execute("docker", "ps", "-aqf", "name=${containerName}");
            Optional.ofNullable(id)
                    .filter(s -> !Strings.isNullOrEmpty(s))
                    .map(i -> i.substring(0, 12))
                    .ifPresent(i -> execute("docker", "stop", i));
        }
        config.ports().forEach((p1, p2) -> cmdLineBuilder.add("-p", "${p1}:${p2}"));
        config.environment().forEach((k, v) -> cmdLineBuilder.add("-e", "${k}=${v}"));
        config.volumes().forEach((l, r) -> cmdLineBuilder.add("-v", "${l}:${r}"));
        cmdLineBuilder.add(config.image());
        config.command().forEach(cmdLineBuilder::add);
        String[] cmdLine = cmdLineBuilder.build().toArray(new String[0]);

        String containerId = execute(cmdLine);
        Optional.ofNullable(config.waitPolicy())
                .ifPresent(policy -> {
                    if (!policy.waitForReady()) {
                        throw new RuntimeException("Could not start container");
                    }
                });
        return Optional.ofNullable(containerId)
                .map(id -> id.substring(0, 12))
                .<AutoCloseable>map(id -> () -> execute("docker", "stop", containerId.substring(0, 12)))
                .orElse(() -> {});
    }

    public static AutoCloseable withCompose(String composePath) {
        start(composePath);
        return () -> stop(composePath);
    }

    private static @Nullable String execute(String... cmdLine) {
        StringBuilder stdOut = new StringBuilder();
        try {
            String cmd = String.join(" ", cmdLine);
            System.out.println("Running: " + cmd);
            Process proc = Runtime.getRuntime().exec(cmdLine);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stdOut.append(line).append("\n");
                    System.out.println(line);
                }
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.err.println(line);
                }
            }
            proc.waitFor();
        } catch (IOException e) {
            return null;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return stdOut.toString();
    }
}
