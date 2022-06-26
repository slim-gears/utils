package com.slimgears.util.reflect;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.stream.Collectors;

public class ReflectUtilsTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    @interface TestAnnotation {
        String value() default "";
    }

    interface Interface {
        @TestAnnotation("Interface.foo")
        void foo(int i, String s);
    }

    interface AnotherInterface {
        @TestAnnotation("AnotherInterface.foo")
        void foo(int i, String s);
    }

    interface EmptyInterface {

    }

    static class Parent implements Interface, AnotherInterface, EmptyInterface {
        @TestAnnotation("Parent.foo")
        @Override
        public void foo(int i, String s) {

        }
    }

    static class Derived extends Parent {
        @TestAnnotation("Derived.foo")
        @Override
        public void foo(int i, String s) {

        }
    }

    static class MoreDerived extends Derived {

    }


    @Test
    public void testHierarchy() {
        var hierarchy = ReflectUtils.classHierarchy(Derived.class).collect(Collectors.toSet());
        Assert.assertEquals(6, hierarchy.size());
        Assert.assertTrue(hierarchy.contains(Interface.class));
        Assert.assertTrue(hierarchy.contains(AnotherInterface.class));
        Assert.assertTrue(hierarchy.contains(EmptyInterface.class));
        Assert.assertTrue(hierarchy.contains(Parent.class));
        Assert.assertTrue(hierarchy.contains(Derived.class));
        Assert.assertTrue(hierarchy.contains(Object.class));
    }

    @Test
    public void testMethodHierarchy() {
        Assert.assertEquals(4L, ReflectUtils.methodHierarchy(Derived.class, "foo", int.class, String.class).count());
        Assert.assertEquals(4L, ReflectUtils.methodHierarchy(MoreDerived.class, "foo", int.class, String.class).count());
        Assert.assertEquals(3L, ReflectUtils.methodHierarchy(Parent.class, "foo", int.class, String.class).count());
        Assert.assertEquals(1L, ReflectUtils.methodHierarchy(Interface.class, "foo", int.class, String.class).count());
        Assert.assertEquals(1L, ReflectUtils.methodHierarchy(AnotherInterface.class, "foo", int.class, String.class).count());
    }
}
