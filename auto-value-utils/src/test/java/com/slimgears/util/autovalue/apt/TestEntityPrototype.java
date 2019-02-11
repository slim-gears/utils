package com.slimgears.util.autovalue.apt;

import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

@AutoValuePrototype
interface TestEntityPrototype {
    String text();
    String description();
}
