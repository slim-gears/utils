package com.slimgears.util.autovalue.apt;

import com.google.auto.common.MoreTypes;
import com.google.auto.value.processor.AutoValueProcessor;
import com.slimgears.apt.AbstractAnnotationProcessor;
import com.slimgears.apt.data.TypeInfo;
import com.slimgears.apt.util.AnnotationProcessingTester;
import com.slimgears.apt.util.StoreWrittenFilesRule;
import com.slimgears.util.generic.ScopedInstance;
import com.slimgears.util.stream.Streams;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.event.Level;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class AutoValuePrototypeAnnotationProcessorTest {
    private ScopedInstance.Closeable registrarScope;
    @ClassRule public final static StoreWrittenFilesRule storeWrittenFilesRule = StoreWrittenFilesRule
            .forPath("build/test-results/files");

    @Before
    public void setUp() {
        registrarScope = Registrar.scope();
    }

    @After
    public void tearDown() {
        registrarScope.close();
    }

    @Test
    public void testBasicPrototype() {
        tester()
                .inputFiles(
                        "SampleFieldAnnotation.java",
                        "SampleValuePrototype.java")
                .expectedSources(
                        "SampleValue.java",
                        "SampleValuePrototypeBuilder.java")
                .test();
    }

    @Test
    public void testGenericPrototype() {
        tester()
                .inputFiles(
                        "SampleInterface.java",
                        "SampleGenericInterface.java",
                        "SampleGenericPrototype.java")
                .expectedSources(
                        "SampleGenericPrototypeBuilder.java",
                        "SampleGeneric.java")
                .test();
    }

    @Test
    public void testComparableGenericPrototype() {
        tester()
                .inputFiles(
                        "SampleComparableGenericInterface.java",
                        "SampleComparableGenericPrototype.java")
                .expectedSources(
                        "SampleComparableGeneric.java",
                        "SampleComparableGenericPrototypeBuilder.java")
                .test();
    }

    @Test
    public void testGenericFieldPrototype() {
        tester()
                .inputFiles("SampleGenericFieldPrototype.java")
                .expectedSources(
                        "SampleGenericFieldPrototypeBuilder.java",
                        "SampleGenericField.java")
                .test();
    }

    @Test
    public void testPrototypeReferences() {
        tester()
                .inputFiles("SampleAPrototype.java", "SampleBPrototype.java")
                .expectedSources("SampleBPrototypeBuilder.java")
                .test();
    }

    @Test
    public void testGuavaValuePrototype() {
        tester()
                .inputFiles("SampleGuavaValuePrototype.java")
                .expectedSources("SampleGuavaValuePrototypeBuilder.java", "SampleGuavaValue.java")
                .test();
    }

    @Test
    public void testCustomBuilderValuePrototype() {
        tester()
                .inputFiles("SampleCustomBuilderValuePrototype.java")
                .expectedSources(
                        "SampleCustomBuilderValue.java",
                        "SampleCustomBuilderValuePrototypeBuilder.java")
                .test();
    }

    @Test
    public void testSpecializedGenericPrototype() {
        tester()
                .inputFiles(
                        "SampleSpecializedGenericPrototype.java",
                        "SampleGuavaInterface.java")
                .expectedSources(
                        "SampleSpecializedGeneric.java",
                        "SampleSpecializedGenericPrototypeBuilder.java")
                .test();
    }

    @Test
    public void testGenericListContainerPrototype() {
        tester()
                .inputFiles(
                        "GenericListItemPrototype.java",
                        "GenericListContainerPrototype.java")
                .expectedSources(
                        "GenericListItem.java",
                        "GenericListContainer.java")
                .test();
    }

    @Test
    public void testSampleWithKeyPrototype() {
        tester()
                .inputFiles("SampleWithKeyPrototype.java")
                .expectedSources("SampleWithKey.java")
                .test();
    }

    @Test
    public void testCircularDependencyPrototype() {
        tester()
                .inputFiles("SampleCircularDependencyPrototype.java")
                .expectedSources("SampleCircularDependency.java")
                .test();
    }

    @Test
    public void testNestedTypePrototype() {
        tester()
                .inputFiles("SampleNestedTypePrototype.java")
                .expectedSources("SampleNestedType.java")
                .test();
    }

    @Test
    public void testCustomAutoAnnotationWithBuilder() {
        tester()
                .inputFiles(
                        "CustomAutoAnnotationWithBuilder.java",
                        "SampleValueUsingCustomProto.java")
                .expectedSources("SampleValueUsingCustomConcreteWithBuilder.java")
                .test();
    }

    @Test
    public void testCustomAutoAnnotationWithCreator() {
        tester()
                .inputFiles(
                        "CustomAutoAnnotationWithCreator.java",
                        "SampleValueUsingCustomProto.java")
                .expectedSources("SampleValueUsingCustomConcreteWithCreator.java")
                .test();
    }

    @Test
    public void testCustomAutoAnnotationOptionalOnlyWithBuilder() {
        tester()
                .inputFiles(
                        "CustomAutoAnnotationWithBuilder.java",
                        "SampleAllOptionalPropertiesProto.java")
                .expectedSources("SampleAllOptionalPropertiesConcreteWithBuilder.java")
                .test();
    }

    @Test
    public void testCustomAutoAnnotationOptionalOnlyWithCreator() {
        tester()
                .inputFiles(
                        "CustomAutoAnnotationWithCreator.java",
                        "SampleAllOptionalPropertiesProto.java")
                .expectedSources("SampleAllOptionalPropertiesConcreteWithCreator.java")
                .test();
    }

    @Test
    public void testCustomAutoAnnotationOptionalOnlyGenericWithBuilder() {
        tester()
                .inputFiles(
                        "CustomAutoAnnotationWithBuilder.java",
                        "GenericAllOptionalPropertiesProto.java")
                .expectedSources("GenericAllOptionalPropertiesConcreteWithBuilder.java")
                .test();
    }

    @Test
    public void testCustomAutoAnnotationOptionalOnlyGenericWithCreator() {
        tester()
                .inputFiles(
                        "CustomAutoAnnotationWithCreator.java",
                        "GenericAllOptionalPropertiesProto.java")
                .expectedSources("GenericAllOptionalPropertiesConcreteWithCreator.java")
                .test();
    }

    @Test
    public void testReservedWordsAsPropertyNames() {
        tester()
                .inputFiles(
                        "ObjectFilterPrototype.java",
                        "ValueFilterPrototype.java")
                .test();
    }

    @Test
    public void testCollectionElementType() {
        AtomicReference<TypeInfo> elementType = new AtomicReference<>();
        AnnotationProcessingTester.create()
                .inputFiles("SampleList.java")
                .processedWith(new AbstractAnnotationProcessor() {
                    @Override
                    public Set<String> getSupportedAnnotationTypes() {
                        return Collections.singleton("com.slimgears.util.autovalue.annotations.AutoValuePrototype");
                    }

                    @Override
                    protected boolean processType(TypeElement annotationType, TypeElement typeElement) {
                        ExecutableElement propertyElement = typeElement.getEnclosedElements()
                                .stream()
                                .flatMap(Streams.ofType(ExecutableElement.class))
                                .findFirst()
                                .get();

                        DeclaredType declaredType = MoreTypes.asDeclared(typeElement.asType());
                        PropertyInfo propertyInfo = PropertyInfo.create(declaredType, propertyElement, false);
                        elementType.set(propertyInfo.collectionElementType());
                        return true;
                    }
                })
                .test();
        Assert.assertEquals(TypeInfo.of("T"), elementType.get());
    }

    private AnnotationProcessingTester tester() {
        return AnnotationProcessingTester.create()
                .verbosity(Level.TRACE)
                .processedWith(new AutoValuePrototypeAnnotationProcessor(), new AutoValueProcessor());
    }
}
