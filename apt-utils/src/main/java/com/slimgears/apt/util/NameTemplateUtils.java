/**
 *
 */
package com.slimgears.apt.util;

import com.slimgears.apt.data.TypeInfo;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.StringLookup;

import javax.lang.model.element.TypeElement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;

public class NameTemplateUtils {
    public static String getTypeName(String template, TypeElement typeElement, TypeInfo[] typeArgs) {
        Map<String, String> typeParams = new HashMap<>();
        TypeInfo typeInfo = TypeInfo.of(typeElement);

        IntStream
                .range(0, typeArgs.length)
                .forEach(i -> typeParams.put(typeInfo.typeParams().get(i).name(), typeArgs[i].simpleName()));

        StringSubstitutor substitutor = createSubstitutor(typeParams::get);
        return substitutor.replace(template);
    }

    public static void validateNameTemplate(String template, TypeElement typeElement) {
        Set<String> namesFromTypeParams = typeElement.getTypeParameters()
                .stream()
                .map(tp -> tp.getSimpleName().toString())
                .collect(Collectors.toSet());

        Set<String> varNames = getVarNames(template);

        checkArgument(varNames.size() <= typeElement.getTypeParameters().size(), "Template variable names number mismatch");
        varNames.forEach(n -> checkArgument(namesFromTypeParams.contains(n), "Template variable name " + n + " does not correspond to type parameter"));
    }

    private static Set<String> getVarNames(String template) {
        Set<String> names = new HashSet<>();
        StringSubstitutor substitutor = createSubstitutor(name -> { names.add(name); return name; });
        substitutor.replace(template);
        return names;
    }

    private static StringSubstitutor createSubstitutor(StringLookup lookup) {
        return new StringSubstitutor(lookup, "${", "}", StringSubstitutor.DEFAULT_ESCAPE);
    }
}
