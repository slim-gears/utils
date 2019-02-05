package com.slimgears.util.autovalue.annotations;

public interface HasKeyProperty<K, T, B extends BuilderPrototype<T, B>> {
    PropertyMeta<T, B, K> keyProperty();
}
