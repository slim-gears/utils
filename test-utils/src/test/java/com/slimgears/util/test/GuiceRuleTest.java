package com.slimgears.util.test;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.slimgears.util.junit.ExtensionRuleRunner;
import com.slimgears.util.test.guice.UseModules;
import com.slimgears.util.test.guice.UseProperties;
import com.slimgears.util.test.guice.UseProviders;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import javax.inject.Named;
import javax.inject.Provider;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(ExtensionRuleRunner.class)
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
    @Mock @Named("annotatedMock") private Runnable annotatedMock;
    @Inject @Named("annotatedMock") private Runnable injectedAnnotatedMock;
    @UseProviders.Binding @Named("annotatedProviderMock") private final Provider<String> annotatedProviderMock = () -> "annotatedProviderMockValue";
    @Inject @Named("annotatedProviderMock") private String injectedAnnotatedProviderMock;

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
    @AnnotationRuleProvider.Qualifier(InjectionTestAnnotation.RuleProvider.class)
    public @interface InjectionTestAnnotation {
        class RuleProvider implements AnnotationRuleProvider<InjectionTestAnnotation> {
            @Inject @Named("test") private String injectedMagicString;

            @Override
            public ExtensionRule provide(InjectionTestAnnotation info) {
                Assert.assertEquals("magic string", injectedMagicString);
                return (method, target) -> {
                    ((GuiceRuleTest)target).magicStringFromAnnotation = injectedMagicString;
                    return () -> {};
                };
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

    @Test
    public void testMockAnnotatedBinding() {
        Assert.assertNotNull(annotatedMock);
        Assert.assertNotNull(injectedAnnotatedMock);
        Assert.assertSame(annotatedMock, injectedAnnotatedMock);
    }

    @Test
    public void testAnnotatedProviderMock() {
        Assert.assertNotNull(annotatedProviderMock);
        Assert.assertEquals("annotatedProviderMockValue", injectedAnnotatedProviderMock);
    }
}
