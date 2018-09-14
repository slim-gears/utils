package com.slimgears.util.stream;

import java.text.DecimalFormat;

public class DoubleUtils {
    private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat();

    static {
        DECIMAL_FORMAT.setMinimumFractionDigits(0);
        DECIMAL_FORMAT.setGroupingUsed(false);
    }

    public static double round(double value, int precisionDigits) {
        double scale = Math.pow(10, precisionDigits);
        return Math.round(value * scale) / scale;
    }

    public static double floor(double value, int precisionDigits) {
        double scale = Math.pow(10, precisionDigits);
        return Math.floor(value * scale) / scale;
    }

    public static String toString(double value) {
        return DECIMAL_FORMAT.format(value);
    }
}
