package com.slimgears.util.autovalue.apt;

import com.google.auto.value.processor.AutoValueProcessor;
import com.slimgears.apt.util.AnnotationProcessingTester;
import com.slimgears.util.generic.ScopedInstance;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.event.Level;

public class AutoValuePrototypeAnnotationProcessorTest {
    private ScopedInstance.Closeable scope;

    @Before
    public void setUp() {
        scope = AutoValuePrototypeAnnotationProcessor.Registrar.scope();
    }

    @After
    public void tearDown() {
        scope.close();
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

    private AnnotationProcessingTester tester() {
        return AnnotationProcessingTester.create()
                .verbosity(Level.TRACE)
                .processedWith(new AutoValuePrototypeAnnotationProcessor(), new AutoValueProcessor());
    }
}
