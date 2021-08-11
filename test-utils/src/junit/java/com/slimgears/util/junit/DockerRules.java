package com.slimgears.util.junit;

import com.slimgears.util.test.containers.ContainerConfig;
import com.slimgears.util.test.containers.DockerUtils;
import lombok.SneakyThrows;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class DockerRules {
    public static TestRule container(ContainerConfig config) {
        return ResourceRules.toRule(() -> DockerUtils.withContainer(config));
    }

    public static TestRule compose() {
        return compose("docker-compose.yaml");
    }

    public static TestRule compose(String composeFilePath) {
        return ResourceRules.toRule(() -> DockerUtils.withCompose(composeFilePath));
    }
}
