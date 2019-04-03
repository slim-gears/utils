package com.slimgears.util.autovalue.apt;

import com.slimgears.util.generic.ScopedInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.type.DeclaredType;
import java.util.Collection;
import java.util.HashSet;

public class Registrar {
    private final static Logger log = LoggerFactory.getLogger(Registrar.class);
    private final static ScopedInstance<Registrar> instance = ScopedInstance.create(new Registrar());
    private final Collection<String> processedElements = new HashSet<>();

    public Registrar() {
        log.debug("Registrar created");
    }

    public boolean needsProcessing(DeclaredType type) {
        boolean processed = processedElements.contains(type.asElement().toString());
        log.debug("Type {} is {} ({} processed types)", type.asElement().toString(), processed ? "processed" : "not processed", processedElements.size());
        return !processed;
    }

    public void processed(DeclaredType type) {
        processedElements.add(type.asElement().toString());
        log.debug("Adding processed type: {} ({} processed types)", type, processedElements.size());
    }

    public static Registrar current() {
        return instance.current();
    }

    public static ScopedInstance.Closeable scope() {
        return instance.scope(new Registrar());
    }
}
