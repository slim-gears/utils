package com.slimgears.nanometer;

import java.util.Arrays;

public interface Taggable<T extends Taggable<T>> {
    T tags(MetricTag... tags);

    default T tag(MetricTag tag) {
        return tags(tag);
    }

    default T tag(String key) {
        return tag(MetricTag.of(key));
    }

    default T tag(String key, Object value) {
        return tag(key, value != null ? value.toString() : null);
    }

    default T tag(String key, String value) {
        return tag(MetricTag.of(key, value));
    }

    default T tags(String... tags) {
        return tags(Arrays.stream(tags).map(MetricTag::of).toArray(MetricTag[]::new));
    }
}
