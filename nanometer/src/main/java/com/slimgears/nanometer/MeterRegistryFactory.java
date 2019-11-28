package com.slimgears.nanometer;

import io.micrometer.core.instrument.MeterRegistry;

public interface MeterRegistryFactory {
    MeterRegistry create();
}
