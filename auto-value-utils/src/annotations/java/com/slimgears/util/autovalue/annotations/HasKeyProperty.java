package com.slimgears.util.autovalue.annotations;

public interface HasKeyProperty<K, T, TB extends BuilderPrototype<T, TB>> {
    PropertyMeta<T, TB, K> keyProperty();
}
