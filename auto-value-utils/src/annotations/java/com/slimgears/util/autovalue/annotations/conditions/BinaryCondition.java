package com.slimgears.util.autovalue.annotations.conditions;

import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;

public interface BinaryCondition<T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>, V> extends Condition<T, TB> {
    Value<V> left();
    Value<V> right();
}
