package com.slimgears.util.autovalue.annotations;

import com.slimgears.util.reflect.TypeToken;

public interface MetaClass<T, B extends BuilderPrototype<T, B>> {
    TypeToken<B> builderClass();
    TypeToken<T> objectClass();
    Iterable<PropertyMeta<T, B, ?>> properties();
    B createBuilder();
}
