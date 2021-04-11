package com.slimgears.util.test;

import com.slimgears.util.generic.ScopedInstance;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
@AnnotationRuleProvider.Qualifier(MyCustomRule.RuleProvider.class)
public @interface MyCustomRule {
    String name();

    class RuleProvider implements AnnotationRuleProvider<MyCustomRule> {
        private static final ScopedInstance<List<String>> scopedNames = ScopedInstance.create();

        public static List<String> names() {
            return scopedNames.current();
        }

        @Override
        public ExtensionRule provide(MyCustomRule info) {
            return (method, target) -> {
                if (scopedNames.current() != null) {
                    scopedNames.current().add(info.name());
                    return () -> scopedNames.current().remove(info.name());
                } else {
                    List<String> names = new ArrayList<>();
                    ScopedInstance.Closeable closeable = scopedNames.scope(names);
                    names.add(info.name());
                    return () -> {
                        names.remove(info.name());
                        closeable.close();
                    };
                }
            };
        }
    }
}
