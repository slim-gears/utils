package com.slimgears.util.autovalue.apt;

import com.google.auto.value.AutoValue;
import com.slimgears.apt.data.TypeInfo;
import com.slimgears.apt.util.ImportTracker;

import javax.annotation.Nullable;
import java.util.Collection;

@AutoValue
public abstract class Context {
    public abstract TypeInfo sourceClass();
    public abstract TypeInfo targetClass();
    public abstract Collection<PropertyInfo> properties();
    public abstract ImportTracker imports();
    @Nullable public abstract PropertyInfo keyProperty();

    public static Builder builder() {
        return new AutoValue_Context.Builder();
    }

    public boolean hasKey() {
        return keyProperty() != null;
    }

    @AutoValue.Builder
    public interface Builder {
        Builder sourceClass(TypeInfo cls);
        Builder targetClass(TypeInfo cls);
        Builder properties(Collection<PropertyInfo> properties);
        Builder imports(ImportTracker imports);
        Builder keyProperty(PropertyInfo keyProperty);
        Context build();
    }
}
