package com.slimgears.util.autovalue.apt.extensions;

import com.google.auto.service.AutoService;
import com.slimgears.apt.data.AnnotationInfo;
import com.slimgears.util.autovalue.annotations.UseCopyAnnotator;
import com.slimgears.util.autovalue.apt.Context;
import com.slimgears.util.autovalue.apt.PropertyInfo;

@AutoService(Annotator.class)
@SupportedAnnotations(UseCopyAnnotator.class)
public class CopyAnnotationsAnnotator implements Annotator {
    @Override
    public Iterable<AnnotationInfo> annotatePropertyMethod(Context context, PropertyInfo property) {
        return property.annotations();
    }
}
