package com.slimgears.util.autovalue.apt;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.slimgears.apt.data.AnnotationInfo;
import com.slimgears.apt.data.TypeInfo;

import java.util.Set;

public interface Annotator {
    Annotator empty = new Annotator() {
        @Override
        public void annotateClass(Context context, Set<AnnotationInfo> annotations) {
        }

        @Override
        public void annotateBuilderClass(Context context, Set<AnnotationInfo> annotations) {
        }

        @Override
        public void annotateBuilderCreator(Context context, Set<AnnotationInfo> annotations) {
        }

        @Override
        public void annotatePropertyGetter(Context context, PropertyInfo propertyInfo, Set<AnnotationInfo> annotations) {
        }

        @Override
        public void annotatePropertyBuilder(Context context, PropertyInfo propertyInfo, Set<AnnotationInfo> annotations) {
        }
    };

    @AutoValue
    abstract class Context {
        public abstract ImmutableList<PropertyInfo> properties();
        public abstract TypeInfo sourceClass();
        public abstract TypeInfo targetClass();

        public static Builder builder() {
            return new AutoValue_Annotator_Context.Builder();
        }

        @AutoValue.Builder
        public interface Builder {
            ImmutableList.Builder<PropertyInfo> propertiesBuilder();
            Builder sourceClass(TypeInfo type);
            Builder targetClass(TypeInfo type);
            Context build();

            default Builder addProperties(Iterable<PropertyInfo> properties) {
                propertiesBuilder().addAll(properties);
                return this;
            }

            default Builder addProperty(PropertyInfo property) {
                propertiesBuilder().add(property);
                return this;
            }
        }
    }

    void annotateClass(Context context, Set<AnnotationInfo> annotations);
    void annotateBuilderClass(Context context, Set<AnnotationInfo> annotations);
    void annotateBuilderCreator(Context context, Set<AnnotationInfo> annotations);
    void annotatePropertyGetter(Context context, PropertyInfo propertyInfo, Set<AnnotationInfo> annotations);
    void annotatePropertyBuilder(Context context, PropertyInfo propertyInfo, Set<AnnotationInfo> annotations);

    default Annotator combine(Annotator annotator) {
        return new Annotator() {
            @Override
            public void annotateClass(Context context, Set<AnnotationInfo> annotations) {
            }

            @Override
            public void annotateBuilderClass(Context context, Set<AnnotationInfo> annotations) {
                Annotator.this.annotateBuilderClass(context, annotations);
                annotator.annotateBuilderClass(context, annotations);
            }

            @Override
            public void annotateBuilderCreator(Context context, Set<AnnotationInfo> annotations) {
                Annotator.this.annotateBuilderCreator(context, annotations);
                annotator.annotateBuilderCreator(context, annotations);
            }

            @Override
            public void annotatePropertyGetter(Context context, PropertyInfo propertyInfo, Set<AnnotationInfo> annotations) {
                Annotator.this.annotatePropertyGetter(context, propertyInfo, annotations);
                annotator.annotatePropertyGetter(context, propertyInfo, annotations);
            }

            @Override
            public void annotatePropertyBuilder(Context context, PropertyInfo propertyInfo, Set<AnnotationInfo> annotations) {
                Annotator.this.annotatePropertyBuilder(context, propertyInfo, annotations);
                annotator.annotatePropertyBuilder(context, propertyInfo, annotations);
            }
        };
    }
}
