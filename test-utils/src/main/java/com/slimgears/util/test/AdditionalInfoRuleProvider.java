package com.slimgears.util.test;

public interface AdditionalInfoRuleProvider<T> {
    ExtensionRule provide(T info);
}
