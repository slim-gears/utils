package com.slimgears.util.repository.query;

import com.google.common.collect.ImmutableList;

public interface HasSortingInfo<T> {
    ImmutableList<SortingInfo<T, ?>> sorting();
}
