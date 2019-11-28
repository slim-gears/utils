package com.slimgears.nanometer;

public interface MetricFilter {
    MetricLevel getLevel(String name, MetricTag... tags);
}
