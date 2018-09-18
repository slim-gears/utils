package com.slimgears.util.autovalue.apt;

import com.google.auto.value.processor.AutoValueProcessor;
import com.slimgears.apt.util.AnnotationProcessingTester;
import org.junit.Test;
import org.slf4j.event.Level;

public class AutoValuePrototypeAnnotationProcessorTest {
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
                        "SampleInterfaceBuilder.java",
                        "SampleGenericInterfaceBuilder.java",
                        "SampleGeneric.java")
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
    public void testGuavaValuePrototype() {
        tester()
                .inputFiles("SampleGuavaValuePrototype.java")
                .expectedSources("SampleGuavaValuePrototypeBuilder.java")
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

    private AnnotationProcessingTester tester() {
        return AnnotationProcessingTester.create()
                .verbosity(Level.TRACE)
                .processedWith(new AutoValuePrototypeAnnotationProcessor(), new AutoValueProcessor());
    }
}
