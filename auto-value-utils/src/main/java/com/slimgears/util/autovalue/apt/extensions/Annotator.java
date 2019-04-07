package com.slimgears.util.autovalue.apt.extensions;

import com.slimgears.apt.data.AnnotationInfo;
import com.slimgears.util.autovalue.apt.Context;
import com.slimgears.util.autovalue.apt.PropertyInfo;

import java.util.Collections;

public interface Annotator {
    default Iterable<AnnotationInfo> annotatePropertyMethod(Context context, PropertyInfo property) {
        return Collections.emptySet();
    }

    default Iterable<AnnotationInfo> annotateCreatorParameter(Context context, PropertyInfo property) {
        return Collections.emptySet();
    }

    default Iterable<AnnotationInfo> annotateCreator(Context context) {
        return Collections.emptySet();
    }

    default Iterable<AnnotationInfo> annotateClass(Context context) {
        return Collections.emptySet();
    }

    default Iterable<AnnotationInfo> annotateNonPropertyMethod(Context context) {
        return Collections.emptySet();
    }
}
