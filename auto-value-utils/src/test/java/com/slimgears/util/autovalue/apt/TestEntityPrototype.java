package com.slimgears.util.autovalue.apt;

import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.util.autovalue.annotations.Reference;

import java.util.Collection;

@AutoValuePrototype
interface TestEntityPrototype<T extends Comparable<T>> {
    String text();
    String description();
    T value();
    @Reference TestReferencedEntity<T> referencedEntity();
    Collection<String> names();
    Collection<T> values();
}
