package com.slimgears.util.generic;

import com.slimgears.util.reflect.TypeToken;

public interface ServiceResolver extends AutoCloseable {
    <T> T resolve(TypeToken<T> token);

    default <T> T resolve(Class<T> cls) {
        return resolve(TypeToken.of(cls));
    }

    default void close() {}
}
