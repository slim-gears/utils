package com.slimgears.util.repository.query;

import com.google.common.collect.ImmutableList;
import com.slimgears.util.autovalue.annotations.HasSelf;
import com.slimgears.util.autovalue.annotations.PropertyMeta;

import java.util.Arrays;

public interface HasProperties<T> {
    ImmutableList<PropertyMeta<T, ?, ?>> properties();

    interface Builder<_B extends Builder<_B, T>, T> extends HasSelf<_B> {
        ImmutableList.Builder<PropertyMeta<T, ?, ?>> propertiesBuilder();

        default <V> _B property(PropertyMeta<T, ?, V> property) {
            propertiesBuilder().add(property);
            return self();
        }

        default _B properties(PropertyMeta<T, ?, ?>... properties) {
            propertiesBuilder().addAll(Arrays.asList(properties));
            return self();
        }

    }
}
