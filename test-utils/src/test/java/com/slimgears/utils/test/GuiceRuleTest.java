package com.slimgears.utils.test;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.slimgears.utils.test.guice.GuiceJUnit;
import com.slimgears.utils.test.guice.UseModules;
import com.slimgears.utils.test.guice.UseProperties;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.Mock;

import javax.inject.Named;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(GuiceJUnit.class)
@UseModules(GuiceRuleTest.TestModule.class)
public class GuiceRuleTest {
    @UseModules.Field private final Module fieldModule = new AbstractModule() {
        @Override
        protected void configure() {
            bind(String.class).annotatedWith(Names.named("fieldModule")).toInstance("fieldModuleWorks");
        }
    };

    @Inject @Named("test") private String injectedMagicString;
    @Inject @Named("fieldModule") private String injectedFromFieldModule;
    @Mock private Runnable injectedMockFromMockito;
    @Inject private Runnable injectedMockFromGuice;
    private String magicStringFromAnnotation;
    @Inject(optional = true) @Named("injectFromAnnotationProperties") String injectedFromAnnotationProperties;

    public static class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(String.class).annotatedWith(Names.named("test")).toInstance("magic string");
            bind(Runnable.class).toInstance(() -> {});
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    @AnnotationMethodRule.Qualifier(InjectionTestAnnotation.Rule.class)
    public @interface InjectionTestAnnotation {
        class Rule implements AnnotationMethodRule<InjectionTestAnnotation> {
            @Inject @Named("test") private String injectedMagicString;

            @Override
            public Statement apply(InjectionTestAnnotation info, Statement base, FrameworkMethod method, Object target) {
                Assert.assertEquals("magic string", injectedMagicString);
                ((GuiceRuleTest)target).magicStringFromAnnotation = injectedMagicString;
                return base;
            }
        }
    }

    @Test
    @InjectionTestAnnotation
    public void testInjectionFromModule() {
        Assert.assertEquals("magic string", injectedMagicString);
        Assert.assertNotNull(injectedMockFromMockito);
        Assert.assertNotNull(injectedMockFromGuice);
        Assert.assertSame(injectedMockFromMockito, injectedMockFromGuice);
        Assert.assertEquals(injectedMagicString, magicStringFromAnnotation);

        injectedMockFromMockito.run();
        verify(injectedMockFromMockito, times(1)).run();
    }

    @Test
    public void testInjectionFromModuleField() {
        Assert.assertEquals("fieldModuleWorks", injectedFromFieldModule);
    }

    @Test
    @UseProperties({
            @UseProperties.Property(name = "injectFromAnnotationProperties", value = "injectFromAnnotationPropertiesWorks")
    })
    public void testWithInjectedParameters() {
        Assert.assertEquals("injectFromAnnotationPropertiesWorks", injectedFromAnnotationProperties);
    }
}
