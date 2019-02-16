package com.slimgears.util.autovalue.queries;

import javax.annotation.Nullable;

public interface HasPagination {
    @Nullable Long limit();
    @Nullable Long skip();

    interface Builder<_B extends Builder<_B>> {
        _B limit(Long limit);
        _B skip(Long skip);
    }
}
