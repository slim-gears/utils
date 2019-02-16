package com.slimgears.util.autovalue.queries;

import javax.annotation.Nullable;

public interface Notification<T> {
    @Nullable T oldObject();
    @Nullable T newObject();
}
