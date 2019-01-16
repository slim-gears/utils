package com.slimgears.utils.test;

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
@AnnotationMethodRule.Qualifier(MyCustomRule.Rule.class)
public @interface MyCustomRule {
    String name();

    class Rule implements AnnotationMethodRule<MyCustomRule> {
        private static ScopedInstance<List<String>> scopedNames = ScopedInstance.create();

        public static List<String> names() {
            return scopedNames.current();
        }

        @Override
        public Statement apply(MyCustomRule info, Statement base, FrameworkMethod method, Object target) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    if (scopedNames.current() != null) {
                        scopedNames.current().add(info.name());
                        base.evaluate();
                        scopedNames.current().remove(info.name());
                    } else {
                        List<String> names = new ArrayList<>();
                        try (ScopedInstance.Closeable ignored = scopedNames.scope(names)) {
                            names.add(info.name());
                            base.evaluate();
                            names.remove(info.name());
                        }
                    }
                }
            };
        }
    }
}
