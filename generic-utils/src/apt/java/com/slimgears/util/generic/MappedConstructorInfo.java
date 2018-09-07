package com.slimgears.util.generic;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import com.slimgears.apt.data.*;

import java.util.Map;

@AutoValue
public abstract class MappedConstructorInfo implements HasParameters {
    public abstract MethodInfo superConstructor();
    public abstract ImmutableMap<String, TypeInfo> classParams();

    public boolean isClassParam(ParamInfo param) {
        return param.hasAnnotation(AutoGeneric.ClassParam.class);
    }

    public TypeInfo getClassParam(ParamInfo param) {
        return classParams().get(param.getAnnotation(AutoGeneric.ClassParam.class).getValue("value").primitive().toString());
    }

    public static Builder builder() {
        return new AutoValue_MappedConstructorInfo.Builder();
    }

    @AutoValue.Builder
    public interface Builder extends InfoBuilder<MappedConstructorInfo>, HasParameters.Builder<Builder> {
        ImmutableMap.Builder<String, TypeInfo> classParamsBuilder();
        Builder superConstructor(MethodInfo sourceConstructor);

        default Builder classParams(Map<String, TypeInfo> args) {
            classParamsBuilder().putAll(args);
            return this;
        }

        default Builder classParam(String param, TypeInfo arg) {
            classParamsBuilder().put(param, arg);
            return this;
        }
    }
}
