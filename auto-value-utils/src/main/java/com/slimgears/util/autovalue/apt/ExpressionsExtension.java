package com.slimgears.util.autovalue.apt;

import com.google.auto.service.AutoService;

@AutoService(Extension.class)
public class ExpressionsExtension implements Extension {
    @Override
    public String generateClassBody(Context context) {
        return context.evaluateResource("extensions-body.java.vm");
    }
}
