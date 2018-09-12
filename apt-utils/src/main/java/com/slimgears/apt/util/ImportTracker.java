/**
 *
 */
package com.slimgears.apt.util;

import com.google.common.collect.ImmutableSet;
import com.slimgears.apt.data.TypeInfo;
import com.slimgears.apt.data.TypeParameterInfo;
import com.slimgears.util.generic.ScopedInstance;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.Callable;

public class ImportTracker {
    private final static ScopedInstance<ImportTracker> scopedInstance = new ScopedInstance<>();
    private final static String importsMagicWord = "`imports`";
    private final Collection<String> imports = new TreeSet<>();
    private final Collection<TypeInfo> usedClasses = new TreeSet<>(TypeInfo.comparator);
    private final Collection<TypeInfo> knownClasses = new HashSet<>();
    private final ImmutableSet<String> knownPackageNames;

    public static ImportTracker create(String... knownPackageNames) {
        return new ImportTracker(knownPackageNames);
    }

    public static ImportTracker current() {
        return scopedInstance.current();
    }

    public static <T> T withTracker(ImportTracker tracker, Callable<? extends T> callable) {
        return scopedInstance.withScope(tracker, callable);
    }

    private ImportTracker(String... knownPackageNames) {
        this.knownPackageNames = ImmutableSet.copyOf(knownPackageNames);
    }

    public static String useType(TypeInfo type) {
        return Optional
                .ofNullable(scopedInstance.current())
                .map(tracker -> tracker.use(type))
                .orElseGet(type::toString);
    }

    public String[] imports() {
        return this.imports.toArray(new String[imports.size()]);
    }

    public TypeInfo[] usedClasses() {
        return this.usedClasses.toArray(new TypeInfo[usedClasses.size()]);
    }

    public ImportTracker knownClass(TypeInfo... typeInfo) {
        knownClasses.addAll(Arrays.asList(typeInfo));
        return this;
    }

    public String use(TypeInfo typeInfo) {
        return simplify(typeInfo).fullName();
    }

    public String use(String cls) {
        TypeInfo typeInfo = TypeInfo.of(cls);
        return use(typeInfo);
    }

    private TypeInfo simplify(TypeInfo typeInfo) {
        if (typeInfo.isWildcard()) {
            return typeInfo;
        }

        if (knownClasses.contains(typeInfo) ||
                (typeInfo.isArray() && knownClasses.contains(typeInfo.elementTypeOrSelf()))) {
            return typeInfo;
        }

        usedClasses.add(typeInfo.isArray()
                ? typeInfo.elementTypeOrSelf()
                : TypeInfo.of(typeInfo.name()));

        String packageName = typeInfo.packageName();
        if (!packageName.isEmpty() && !knownPackageNames.contains(packageName)) {
            TypeInfo importType = typeInfo.isArray() ? typeInfo.elementTypeOrSelf() : typeInfo;
            imports.add(importType.name());
        }

        TypeInfo.Builder builder = TypeInfo.builder().name(typeInfo.simpleName());
        typeInfo.typeParams().stream()
                .map(this::simplify)
                .forEach(builder::typeParam);

        return builder.build();
    }

    private TypeParameterInfo simplify(TypeParameterInfo typeParameter) {
        return TypeParameterInfo
                .builder()
                .name(typeParameter.name())
                .bounding(simplify(typeParameter.bounding()))
                .type(simplify(typeParameter.type()))
                .build();
    }

    private TypeParameterInfo.BoundInfo simplify(TypeParameterInfo.BoundInfo boundInfo) {
        if (boundInfo == null) {
            return null;
        }
        if (boundInfo.kind() == TypeParameterInfo.BoundInfo.Kind.BoundExtends) {
            return TypeParameterInfo.BoundInfo.ofBoundExtends(simplify(boundInfo.boundExtends()));
        } else {
            return TypeParameterInfo.BoundInfo.ofBoundSuper(simplify(boundInfo.boundSuper()));
        }
    }

    @Override
    public String toString() {
        return importsMagicWord;
    }
}
