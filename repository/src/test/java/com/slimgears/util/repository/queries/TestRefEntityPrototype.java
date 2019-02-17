package com.slimgears.util.repository.queries;

import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.util.autovalue.annotations.Key;
import com.slimgears.util.repository.annotations.AutoValueExpressions;

@AutoValuePrototype
@AutoValueExpressions
public interface TestRefEntityPrototype {
    @Key int id();
    String text();
}
