package com.slimgears.util.autovalue.apt.extensions;

import com.google.auto.service.AutoService;
import com.google.auto.value.AutoValue;
import com.slimgears.apt.data.AnnotationInfo;
import com.slimgears.apt.data.TypeInfo;
import com.slimgears.util.autovalue.annotations.UseAutoValueAnnotator;
import com.slimgears.util.autovalue.apt.Context;

import java.util.Collections;

@AutoService(Annotator.class)
@SupportedAnnotations(UseAutoValueAnnotator.class)
public class AutoValueAnnotator implements Annotator {
    @Override
    public Iterable<AnnotationInfo> annotateClass(Context context) {
        return Collections.singleton(AnnotationInfo.ofType(TypeInfo.of(AutoValue.class)));
    }

    @Override
    public Iterable<AnnotationInfo> annotateBuilderClass(Context context) {
        return Collections.singleton(AnnotationInfo.ofType(TypeInfo.of(AutoValue.Builder.class)));
    }
}
