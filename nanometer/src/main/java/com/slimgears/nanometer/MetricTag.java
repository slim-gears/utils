package com.slimgears.nanometer;

public interface MetricTag {
    String key();
    String value();

    static MetricTag of(String key, String value) {
        return new MetricTag() {
            @Override
            public String key() {
                return key;
            }

            @Override
            public String value() {
                return value;
            }
        };
    }

    static MetricTag of(String key) {
        return of(key, "");
    }
}
