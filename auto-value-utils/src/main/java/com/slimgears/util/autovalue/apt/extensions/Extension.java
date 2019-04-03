package com.slimgears.util.autovalue.apt.extensions;

import com.slimgears.apt.data.TypeInfo;
import com.slimgears.util.autovalue.apt.Context;

import java.util.Collections;

public interface Extension {
    default boolean isApplicable(Context context) {
        return true;
    }

    default String generateClassBody(Context context) {
        return "";
    }

    default Iterable<TypeInfo> getInterfaces(Context context) {
        return Collections.emptyList();
    }
}
