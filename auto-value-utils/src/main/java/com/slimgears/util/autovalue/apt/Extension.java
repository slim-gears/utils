package com.slimgears.util.autovalue.apt;

import com.slimgears.apt.data.TypeInfo;

import java.util.Collections;

public interface Extension {
    default boolean isApplicable(Context context) {
        return true;
    }

    default String generateClassBody(Context context) {
        return "";
    }

    default String generateBuilderBody(Context context) {
        return "";
    }

    default Iterable<TypeInfo> getInterfaces(Context context) {
        return Collections.emptyList();
    }
}
