package com.slimgears.util.repository.apt;

import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.util.repository.annotations.AutoValueExpressions;

@AutoValuePrototype
@AutoValueExpressions
interface TestReferencedEntityPrototype<T> {
    String text();
    String description();
    T value();
}
