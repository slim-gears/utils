package com.slimgears.util.repository.query;

import com.google.common.collect.ImmutableList;

public interface HasPropertyUpdates<T> {
    ImmutableList<PropertyUpdateInfo<T, ?>> propertyUpdates();
}
