package com.slimgears.sample;

public interface SampleInterface {
    String value();

    default String defaultValue() {
        return "default" + value();
    }
}
