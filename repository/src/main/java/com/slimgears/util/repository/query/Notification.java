package com.slimgears.util.repository.query;

import javax.annotation.Nullable;

public interface Notification<T> {
    @Nullable T oldObject();
    @Nullable T newObject();
}
