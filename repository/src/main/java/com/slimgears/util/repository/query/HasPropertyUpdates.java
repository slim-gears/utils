package com.slimgears.util.repository.query;

import com.google.common.collect.ImmutableList;
import com.slimgears.util.autovalue.annotations.HasSelf;

import java.util.Arrays;

public interface HasPropertyUpdates<T> {
    ImmutableList<PropertyUpdateInfo<T, ?>> propertyUpdates();

    interface Builder<_B extends Builder<_B, T>, T> extends HasSelf<_B> {
        ImmutableList.Builder<PropertyUpdateInfo<T, ?>> propertyUpdatesBuilder();

        default _B propertyUpdate(PropertyUpdateInfo<T, ?> update) {
            propertyUpdatesBuilder().add(update);
            return self();
        }

        default _B propertyUpdates(PropertyUpdateInfo<T, ?>... updates) {
            propertyUpdatesBuilder().addAll(Arrays.asList(updates));
            return self();
        }
    }
}
