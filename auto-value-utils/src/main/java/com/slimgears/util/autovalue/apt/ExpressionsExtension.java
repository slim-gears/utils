package com.slimgears.util.autovalue.apt;

import com.google.auto.service.AutoService;
import com.slimgears.apt.util.TemplateEvaluator;

@AutoService(Extension.class)
public class ExpressionsExtension implements Extension {
    @Override
    public String generateClassBody(Context context) {
        return TemplateEvaluator
                .forResource("extensions-body.java.vm")
                .variables(context)
                .variable("dollar", "$")
                .evaluate();
    }
}
