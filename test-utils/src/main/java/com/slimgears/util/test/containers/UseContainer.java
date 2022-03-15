package com.slimgears.util.test.containers;

import com.google.common.base.Strings;
import com.slimgears.util.test.AnnotationRuleProvider;
import com.slimgears.util.test.ExtensionRule;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.Arrays;

@AnnotationRuleProvider.Qualifier(UseContainer.Provider.class)
public @interface UseContainer {
    String composeFile() default "docker-compose.yaml";
    String image() default "";
    String containerName() default "";
    String[] command() default {};
    Port[] ports() default {};
    EnvVar[] environment() default {};
    Volume[] volumes() default {};
    int waitSeconds() default 5;
    Class<? extends WaitPolicy> waitPolicy() default WaitPolicy.class;

    @interface EnvVar {
        String name();
        String value();
    }

    @interface Volume {
        String localPath();
        String remotePath();
    }

    @interface Port {
        int value() default -1;
        int localPort() default -1;
        int containerPort() default -1;
    }

    class Provider implements AnnotationRuleProvider<UseContainer> {
        @Override
        public ExtensionRule provide(UseContainer info) {
            return Strings.isNullOrEmpty(info.image())
                    ? provideForContainer(info)
                    : provideForCompose(info.composeFile());
        }

        private ExtensionRule provideForCompose(String composeFilePath) {
            return (method, target) -> DockerUtils.withCompose(composeFilePath);
        }

        private ExtensionRule provideForContainer(UseContainer info) {
            return (method, target) -> DockerUtils.withContainer(toContainerConfig(info));
        }

        @SneakyThrows
        private ContainerConfig toContainerConfig(UseContainer info) {
            ContainerConfig.Builder builder = ContainerConfig.builder()
                    .containerName(info.containerName())
                    .image(info.image());
            builder.commandAdd(info.command());
            Arrays.asList(info.ports()).forEach(p -> {
                if (p.value() > 0) {
                    builder.portsPut(p.value(), p.value());
                } else if (p.localPort() > 0 && p.containerPort() > 0) {
                    builder.portsPut(p.localPort(), p.containerPort());
                }
            });
            Arrays.asList(info.environment()).forEach(v -> builder.environmentPut(v.name(), v.value()));
            Arrays.asList(info.volumes()).forEach(v -> builder.volumesPut(v.localPath(), v.remotePath()));

            WaitPolicy waitPolicy = WaitPolicy.delaySeconds(info.waitSeconds());

            if (info.waitPolicy() != WaitPolicy.class) {
                waitPolicy = info.waitPolicy().getConstructor().newInstance();
            }

            builder.waitPolicy(waitPolicy);

            return builder.build();
        }
    }
}
