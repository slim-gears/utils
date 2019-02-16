package com.slimgears.util.autovalue.apt;

import com.slimgears.util.autovalue.annotations.AutoValueExpressions;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

@AutoValuePrototype
@AutoValueExpressions
interface TestReferencedEntityPrototype<T> {
    String text();
    String description();
    T value();
}
