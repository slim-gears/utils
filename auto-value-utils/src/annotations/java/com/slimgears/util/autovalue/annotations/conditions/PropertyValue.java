package com.slimgears.util.autovalue.annotations.conditions;

import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.PropertyMeta;

public interface PropertyValue<T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>, V> {
    PropertyMeta<T, TB, V> property();
}
