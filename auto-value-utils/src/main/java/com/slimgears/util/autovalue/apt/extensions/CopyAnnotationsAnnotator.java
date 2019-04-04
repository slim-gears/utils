package com.slimgears.util.autovalue.apt.extensions;

import com.slimgears.apt.data.AnnotationInfo;
import com.slimgears.util.autovalue.apt.Context;
import com.slimgears.util.autovalue.apt.PropertyInfo;

public class CopyAnnotationsAnnotator implements Annotator {
    @Override
    public Iterable<AnnotationInfo> annotatePropertyMethod(Context context, PropertyInfo property) {
        return property.annotations();
    }
}
