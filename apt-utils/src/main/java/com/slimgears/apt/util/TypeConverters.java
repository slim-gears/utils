/**
 */
package com.slimgears.apt.util;

import com.slimgears.apt.data.Environment;
import com.slimgears.apt.data.TypeInfo;
import com.slimgears.util.guice.ConfigProviders;
import com.slimgears.util.stream.Safe;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.slimgears.util.guice.ConfigProviders.loadFromFile;
import static com.slimgears.util.guice.ConfigProviders.loadFromResource;
import static com.slimgears.util.stream.Streams.ofType;

public class TypeConverters {
    public static TypeConverter empty = create(t -> false, (u, t) -> t);

    public static TypeConverter fromProperties(Properties properties) {
        return properties.stringPropertyNames()
                .stream()
                .map(fromType -> createConverter(fromType, properties.getProperty(fromType)))
                .reduce(TypeConverter::combineWith)
                .orElse(TypeConverters.empty);
    }

    public static TypeConverter fromEnvironmentMaps(String typeMapsKey) {
        return Optional
                .ofNullable(Environment.instance().properties().get(typeMapsKey))
                .map(typeMaps -> typeMaps.split(","))
                .map(Stream::of)
                .orElseGet(Stream::empty)
                .map(String::trim)
                .map(Paths::get)
                .map(TypeConverters::fromPropertiesFile)
                .reduce(TypeConverter::combineWith)
                .orElse(empty);
    }

    @SuppressWarnings("unchecked")
    public static TypeConverter fromEnvironmentConverters(String typeConvertersKey) {
        return Optional
                .ofNullable(Environment.instance().properties().get(typeConvertersKey))
                .map(converters -> converters.split(","))
                .map(Stream::of)
                .orElseGet(Stream::empty)
                .map(String::trim)
                .map(Safe.ofFunction(Class::forName))
                .filter(TypeConverter.class::isAssignableFrom)
                .map(cls -> (Class<? extends TypeConverter>)cls)
                .map(Safe.ofFunction(Class::newInstance))
                .flatMap(ofType(TypeConverter.class))
                .reduce(TypeConverter::combineWith)
                .orElse(empty);
    }

    public static TypeConverter fromPropertiesFile(Path path) {
        return fromProperties(ConfigProviders.create(loadFromFile(path)));
    }

    public static TypeConverter fromPropertiesResource(String resourcePath) {
        return fromProperties(ConfigProviders.create(loadFromResource(resourcePath)));
    }

    public static TypeConverter ofMultiple(TypeConverter... converters) {
        return new TypeConverter() {
            @Override
            public boolean canConvert(TypeInfo typeInfo) {
                return Stream.of(converters).anyMatch(c -> c.canConvert(typeInfo));
            }

            @Override
            public TypeInfo convert(TypeConverter upstream, TypeInfo typeInfo) {
                return Stream
                        .of(converters)
                        .filter(c -> c.canConvert(typeInfo))
                        .map(c -> c.convert(upstream, typeInfo))
                        .findFirst()
                        .orElse(typeInfo);
            }
        };
    }

    public static TypeConverter create(Predicate<TypeInfo> predicate, BiFunction<TypeConverter, TypeInfo, TypeInfo> converter) {
        return new TypeConverter() {
            @Override
            public boolean canConvert(TypeInfo typeInfo) {
                return predicate.test(typeInfo);
            }

            @Override
            public TypeInfo convert(TypeConverter upstream, TypeInfo typeInfo) {
                return (canConvert(typeInfo))
                        ? converter.apply(upstream, typeInfo)
                        : typeInfo;
            }
        };
    }

    public static TypeConverter create(Predicate<TypeInfo> predicate, Function<TypeInfo, TypeInfo> converter) {
        return create(predicate, (up, type) -> converter.apply(type));
    }

    private static TypeConverter createConverter(String from, String to) {
        if (isWildcard(from)) {
            return TypeConverters.create(matchWildcard(from), (upstream, type) -> typeFromWildcard(upstream, type, from, to));
        }

        TypeInfo pattern = TypeInfo.of(from);
        return TypeConverters.create(
                type -> areTypesMatching(pattern, type),
                (upstream, type) -> typeFromTemplate(upstream, pattern, type, to));
    }

    private static boolean areTypesMatching(TypeInfo left, TypeInfo right) {
        if (left.arrayDimensions() == 0 && left.fullName().startsWith("$")) {
            return true;
        }

        if (left.arrayDimensions() != right.arrayDimensions()) {
            return false;
        }

        if (!left.isArray() && !Objects.equals(left.name(), right.name())) {
            return false;
        }

        if (left.typeParams().size() != right.typeParams().size()) {
            return false;
        }

        return IntStream.range(0, left.typeParams().size())
                .allMatch(i -> areTypesMatching(left.typeParams().get(i).type(), right.typeParams().get(i).type()));
    }

    private static Pattern patternFromWildcard(String wildcard) {
        wildcard = wildcard.substring(1, wildcard.length() - 1);
        String regex = "^" + wildcard
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("*", "\\s*([a-zA-Z0-9_$<,>]+)\\s*") + "$";
        return Pattern.compile(regex);
    }

    private static Predicate<TypeInfo> matchWildcard(String wildcard) {
        Pattern pattern = patternFromWildcard(wildcard);
        return type -> pattern.matcher(type.fullName()).matches();
    }

    private static TypeInfo typeFromWildcard(TypeConverter upstream, TypeInfo sourceType, String wildcard, String template) {
        if (template.isEmpty()) {
            return sourceType;
        }

        Pattern pattern = patternFromWildcard(wildcard);
        Matcher matcher = pattern.matcher(sourceType.fullName());
        if (!matcher.matches()) {
            return sourceType;
        }

        for (int i = 1; i < matcher.groupCount() + 1; ++i) {
            template = template.replace("$" + i, matcher.group(i));
        }
        return toTypeInfo(template);
    }

    private static TypeInfo typeFromTemplate(TypeConverter upstream, TypeInfo patternType, TypeInfo sourceType, String template) {
        if (template.isEmpty()) {
            return sourceType;
        }

        Map<String, TypeInfo> paramMap = new HashMap<>();
        if (patternType.isArray()) {
            String name = patternType.elementTypeOrSelf().fullName();
            TypeInfo type = sourceType.elementTypeOrSelf();
            paramMap.put(name, upstream.convert(upstream, type));
        } else {
            IntStream.range(0, patternType.typeParams().size())
                    .forEach(i -> {
                        String name = patternType.typeParams().get(i).type().name();
                        TypeInfo type = sourceType.typeParams().get(i).type();
                        paramMap.put(name, upstream.convert(upstream, type));
                    });
        }

        for (Map.Entry<String, TypeInfo> entry : paramMap.entrySet()) {
            String varRef = entry.getKey();
            template = template.replace(varRef, entry.getValue().fullName());
        }

        return toTypeInfo(template);
    }

    private static TypeInfo toTypeInfo(String template) {
        return isWildcard(template)
                ? TypeInfo.builder().name(template.substring(1, template.length() - 1)).build()
                : TypeInfo.of(template);
    }

    private static boolean isWildcard(String template) {
        return template.startsWith("`") && template.endsWith("`");
    }
}
