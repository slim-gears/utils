package com.slimgears.util.autovalue.apt.extensions;

import com.google.auto.service.AutoService;
import com.slimgears.util.autovalue.apt.Context;

@AutoService(Extension.class)
public class BuilderExtension implements Extension {
    @Override
    public String generateClassBody(Context context) {
        return context.evaluateResource("builder-body.java.vm");
    }
}
