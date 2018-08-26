package com.slimgears.apt.util;

import com.slimgears.apt.data.TypeInfo;

import static com.slimgears.apt.util.TypeConverters.ofMultiple;

public interface TypeConverter {
    boolean canConvert(TypeInfo typeInfo);
    TypeInfo convert(TypeConverter upstream, TypeInfo typeInfo);

    default TypeInfo convert(TypeInfo typeInfo) {
        return convert(this, typeInfo);
    }

    default TypeConverter combineWith(TypeConverter converter) {
        return TypeConverters.create(
                type -> canConvert(type) || converter.canConvert(type),
                (upstream, type) -> canConvert(type)
                        ? convert(upstream, type)
                        : converter.convert(upstream, type));
    }

    default TypeConverter combineWith(TypeConverter... converters) {
        return combineWith(ofMultiple(converters));
    }
}
