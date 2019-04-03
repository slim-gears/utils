package com.slimgears.util.autovalue.apt.extensions;

import com.slimgears.apt.data.TypeInfo;
import com.slimgears.util.autovalue.apt.Context;
import com.slimgears.util.stream.Streams;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompositeExtension implements Extension {
    private final Collection<Extension> extensions;

    public CompositeExtension(String... extensions) {
        this.extensions = Extensions.fromStrings(Extension.class, extensions);
    }

    public CompositeExtension(String[]... extensions) {
        String[] allExtensions = Arrays.stream(extensions)
                .flatMap(Arrays::stream)
                .distinct()
                .toArray(String[]::new);

        this.extensions = Extensions.fromStrings(Extension.class, allExtensions);
    }

    private CompositeExtension(Extension[] extensions) {
        this.extensions = Arrays.asList(extensions);
    }

    public static Extension of(Extension... extensions) {
        return new CompositeExtension(extensions);
    }

    @Override
    public String generateClassBody(Context context) {
        return extensions(context)
                .map(e -> e.generateClassBody(context))
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
