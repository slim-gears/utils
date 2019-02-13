package com.slimgears.util.autovalue.apt;

import com.google.common.collect.ImmutableList;
import com.slimgears.util.stream.Streams;

import java.util.Collection;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class CompositeExtension implements Extension {
    private final Collection<Extension> extensions = ImmutableList.copyOf(ServiceLoader.load(Extension.class, Extension.class.getClassLoader()));

    @Override
    public String generateClassBody(Context context) {
        return Streams
                .fromIterable(extensions)
                .map(e -> e.generateClassBody(context))
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String generateMetaBody(Context context) {
        return Streams
                .fromIterable(extensions)
                .map(e -> e.generateMetaBody(context))
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String generateBuilderBody(Context context) {
        return Streams
                .fromIterable(extensions)
                .map(e -> e.generateBuilderBody(context))
                .collect(Collectors.joining("\n"));
    }
}
