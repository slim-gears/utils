package com.slimgears.util.autovalue.apt;

import com.slimgears.apt.data.TypeInfo;
import com.slimgears.util.stream.Streams;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompositeExtension implements Extension {
    private final Collection<Extension> extensions;

    public CompositeExtension(String... extensions) {
        this.extensions = Arrays
                .stream(extensions)
                .map(CompositeExtension::createExtension)
                .collect(Collectors.toList());
    }

    private CompositeExtension(Extension... extensions) {
        this.extensions = Arrays.asList(extensions);
    }

    public static Extension of(Extension... extensions) {
        return new CompositeExtension(extensions);
    }

    private static Extension createExtension(String fullname) {
        try {
            Class cls = Class.forName(fullname);
            if (!Extension.class.isAssignableFrom(cls)) {
                throw new RuntimeException("Class " + fullname + " is not a valid extension");
            }
            return (Extension)cls.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generateClassBody(Context context) {
        return extensions(context)
                .map(e -> e.generateClassBody(context))
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String generateBuilderBody(Context context) {
        return extensions(context)
                .map(e -> e.generateBuilderBody(context))
                .collect(Collectors.joining("\n"));
    }

    @Override
    public Iterable<TypeInfo> getInterfaces(Context context) {
        return extensions(context)
                .flatMap(e -> Streams.fromIterable(e.getInterfaces(context)))
                .collect(Collectors.toSet());
    }

    private Stream<Extension> extensions(Context context) {
        return extensions.stream().filter(e -> e.isApplicable(context));
    }
}
