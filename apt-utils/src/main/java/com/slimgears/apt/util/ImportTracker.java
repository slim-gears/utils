/**
 *
 */
package com.slimgears.apt.util;

import com.google.common.collect.ImmutableSet;
import com.slimgears.apt.data.TypeInfo;
import com.slimgears.apt.data.TypeParameterInfo;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public class ImportTracker {
    private final static String importsMagicWord = "`imports`";
    private final Collection<String> imports = new TreeSet<>();
    private final Collection<TypeInfo> usedClasses = new TreeSet<>(TypeInfo.comparator);
    private final Collection<TypeInfo> knownClasses = new HashSet<>();
    private final ImmutableSet<String> knownPackageNames;

    public static ImportTracker create(String... knownPackageNames) {
        return new ImportTracker(knownPackageNames);
    }

    private ImportTracker(String... knownPackageNames) {
        this.knownPackageNames = ImmutableSet.copyOf(knownPackageNames);
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
        typeInfo.typeParams().stream().map(TypeParameterInfo::type)
                .map(this::simplify)
                .forEach(builder::typeParams);
        return builder.build();
    }

    @Override
    public String toString() {
        return importsMagicWord;
    }
}
