package com.slimgears.util.autovalue.apt.extensions;

import com.google.auto.service.AutoService;
import com.slimgears.util.autovalue.annotations.UseCreatorExtension;
import com.slimgears.util.autovalue.apt.Context;

@AutoService(Extension.class)
@SupportedAnnotations(UseCreatorExtension.class)
public class CreatorExtension implements Extension {
    @Override
    public String generateClassBody(Context context) {
        return context.evaluateResource("creator-body.java.vm");
    }
}
