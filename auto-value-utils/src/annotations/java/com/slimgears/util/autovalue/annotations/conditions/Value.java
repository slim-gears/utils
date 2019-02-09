package com.slimgears.util.autovalue.annotations.conditions;

public interface Value<V> {
    enum Kind {
        Constant,
        Property
    }
}
