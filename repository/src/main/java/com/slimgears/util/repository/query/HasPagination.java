package com.slimgears.util.repository.query;

import javax.annotation.Nullable;

public interface HasPagination {
    @Nullable Long limit();
    @Nullable Long skip();
}
