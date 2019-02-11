package com.slimgears.util.autovalue.annotations;

public interface MetaClassWithKey<K, T, TB extends BuilderPrototype<T, TB>> extends MetaClass<T, TB>, HasKeyProperty<K, T, TB> {
}
