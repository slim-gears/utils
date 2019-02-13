package com.slimgears.util.autovalue.apt;

public interface Extension {
    default String generateClassBody(Context context) {
        return "";
    }

    default String generateMetaBody(Context context) {
        return "";
    }

    default String generateBuilderBody(Context context) {
        return "";
    }
}
