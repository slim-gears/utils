package com.slimgears.nanometer;

import java.util.Iterator;
import java.util.function.Supplier;

public class MetricTags implements Iterable<MetricTag>, Supplier<MetricTag[]> {
    private final MetricTag[] tags;

    private MetricTags(MetricTag... tags) {
        this.tags = tags;
    }

    public static MetricTags of(MetricTag... tags) {
        return new MetricTags(tags);
    }

    public MetricTag[] combineWith(MetricTag... tags) {
        return combine(this.tags, tags);
    }

    @Override
    public MetricTag[] get() {
        return tags;
    }

    static MetricTag[] combine(MetricTag[] tags, MetricTag... additionalTags) {
        if (additionalTags.length == 0) {
            return tags;
        }

        if (tags.length == 0) {
            return additionalTags;
        }

        MetricTag[] newTags = new MetricTag[tags.length + additionalTags.length];
        System.arraycopy(tags, 0, newTags, 0, tags.length);
        System.arraycopy(additionalTags, 0, newTags, tags.length, additionalTags.length);
        return newTags;
    }

    @Override
    public Iterator<MetricTag> iterator() {
        return new Iterator<MetricTag>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return tags.length > index;
            }

            @Override
            public MetricTag next() {
                return tags[index++];
            }
        };
    }
}
