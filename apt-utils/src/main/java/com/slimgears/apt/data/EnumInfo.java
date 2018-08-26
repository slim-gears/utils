package com.slimgears.apt.data;

import com.google.auto.value.AutoValue;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.slimgears.apt.util.ElementUtils;

import javax.lang.model.element.TypeElement;
import java.util.Arrays;

@AutoValue
public abstract class EnumInfo implements HasType {
    public abstract ImmutableList<EnumValueInfo> constants();

    public static Builder builder() {
        return new AutoValue_EnumInfo.Builder();
    }

    public static EnumInfo of(Class cls) {
        Preconditions.checkArgument(cls.isEnum());
        Builder builder = builder().type(cls);
        //noinspection unchecked
        Arrays.asList(((Class<? extends Enum>)cls).getEnumConstants()).forEach(builder::enumValue);
        return builder.build();
    }

    public static EnumInfo of(TypeElement typeElement) {
        Preconditions.checkArgument(ElementUtils.isEnum(typeElement));
        Builder builder = builder().type(typeElement);
        typeElement.getEnclosedElements()
                .stream()
                .filter(ElementUtils::isEnumConstant)
                .map(Object::toString)
                .forEach(builder::enumValue);
        return builder.build();
    }

    @AutoValue.Builder
    public interface Builder extends InfoBuilder<EnumInfo>, HasType.Builder<Builder> {
        ImmutableList.Builder<EnumValueInfo> constantsBuilder();

        default Builder enumValue(EnumValueInfo value) {
            constantsBuilder().add(value);
            return this;
        }

        default Builder enumValue(String value) {
            return enumValue(EnumValueInfo.of(value));
        }

        default Builder enumValue(Enum value) {
            return enumValue(EnumValueInfo.of(value));
        }
    }
}
