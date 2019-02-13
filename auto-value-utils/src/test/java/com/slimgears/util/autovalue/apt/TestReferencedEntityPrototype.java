package com.slimgears.util.autovalue.apt;

import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

@AutoValuePrototype
interface TestReferencedEntityPrototype<T> {
    String text();
    String description();
    T value();
}
