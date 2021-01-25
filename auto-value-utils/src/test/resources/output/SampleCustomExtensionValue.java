package com.slimgears.sample;

import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleCustomExtensionValue implements SampleCustomExtensionValuePrototype {

    public static SampleCustomExtensionValue create(
         int intValue) {
        return SampleCustomExtensionValue.builder()
            .intValue(intValue)
            .build();
    }

    public abstract Builder toBuilder();

    public static Builder builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleCustomExtensionValue, Builder>, SampleCustomExtensionValuePrototypeBuilder<Builder>{
        public static Builder create() {
            return new AutoValue_SampleCustomExtensionValue.Builder();
        }

        @Override
        Builder intValue(int intValue);
    }

    @Override public abstract int intValue();

}
