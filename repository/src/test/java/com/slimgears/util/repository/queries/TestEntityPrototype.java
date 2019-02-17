package com.slimgears.util.repository.queries;

import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.util.autovalue.annotations.Key;
import com.slimgears.util.autovalue.annotations.Reference;
import com.slimgears.util.repository.annotations.AutoValueExpressions;

import java.util.Collection;

@AutoValuePrototype
@AutoValueExpressions
public interface TestEntityPrototype {
    @Key
    TestKey key();
    String text();
    int number();
    @Reference
    TestRefEntity refEntity();
    Collection<TestRefEntity> refEntities();
}
