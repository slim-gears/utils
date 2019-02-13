package com.slimgears.util.autovalue.apt;

import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.util.autovalue.annotations.Reference;

@AutoValuePrototype
interface TestEntityPrototype<T extends Comparable<T>> {
    String text();
    String description();
    T value();
    @Reference TestReferencedEntity<T> referencedEntity();
}
