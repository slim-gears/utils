package com.slimgears.util.autovalue.annotations.conditions;

import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;

public interface Condition<T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>> {
    enum Kind {
        IsNull,
        IsNotNull,
        Equals
    }

    Kind type();

    Condition<T, TB> and(Condition<T, TB> condition);
    Condition<T, TB> or(Condition<T, TB> condition);
}
