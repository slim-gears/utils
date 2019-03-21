package com.slimgears.util.autovalue.annotations;

public interface HasKeyProperty<K, T> {
    PropertyMeta<T, K> keyProperty();
}
