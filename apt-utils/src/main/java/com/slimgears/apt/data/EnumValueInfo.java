package com.slimgears.apt.data;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class EnumValueInfo implements HasName {
    public static EnumValueInfo of(String name) {
        return new AutoValue_EnumValueInfo.Builder().name(name).build();
    }

    public static EnumValueInfo of(Enum val) {
        return of(val.name());
    }

    @AutoValue.Builder
    public interface Builder extends InfoBuilder<EnumValueInfo>, HasName.Builder<Builder> {

    }
}
