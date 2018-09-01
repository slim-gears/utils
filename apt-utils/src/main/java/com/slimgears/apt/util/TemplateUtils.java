/**
 *
 */
package com.slimgears.apt.util;

import java.util.function.Function;
import java.util.regex.Pattern;

public class TemplateUtils {
    public final static TemplateUtils instance = new TemplateUtils();
    private final static Pattern camelCasePattern = Pattern.compile("([a-z])([A-Z])");

    public static String camelCaseToDash(String camelCase) {
        return camelCasePattern.matcher(camelCase).replaceAll("$1-$2").toLowerCase();
    }

    public static Function<String, String> postProcessImports(ImportTracker importTracker) {
        return postProcessImports(importTracker, type -> type);
    }

    public static Function<String, String> postProcessImports(ImportTracker importTracker, Function<String, String> typeMapper) {
        return code -> substituteImports(importTracker, code, typeMapper);
    }

    private static String substituteImports(ImportTracker importTracker, String code, Function<String, String> typeMapper) {
        StringBuilder builder = new StringBuilder();
        int len = code.length();
        int prevPos = 0;
        int pos = 0;
        while ((pos = code.indexOf("$[", pos)) >= 0) {
            int count = 1;
            int endPos = pos + 2;
            while (count > 0 && endPos < len) {
                if (code.charAt(endPos) == '[') ++count;
                else if (code.charAt(endPos) == ']') --count;
                ++endPos;
            }

            String type = typeMapper.apply(code.substring(pos + 2, endPos - 1));
            builder.append(code, prevPos, pos);
            builder.append(importTracker.use(type));
            prevPos = pos = endPos;
        }
        builder.append(code, prevPos, len);
        return builder.toString();
    }

    public static String preProcessWhitespace(String template) {
        template = template.replace("\r\n", "\n").replace("\r", "\n");
        Pattern redundantWhiteSpace = Pattern.compile("(##\\n) +([^ ])", Pattern.DOTALL);
        return redundantWhiteSpace.matcher(template).replaceAll("$1$2");
    }

    public static String postProcessWhitespace(String template) {
        template = template.replace("\r\n", "\n").replace("\r", "\n");
        Pattern redundantWhiteSpace = Pattern.compile("([^\\s]+[ ])[ ]+([^ ])", Pattern.DOTALL);
        Pattern doubleNewLines = Pattern.compile("\\n\\s*\\n\\s*\\n", Pattern.DOTALL);
        template = redundantWhiteSpace.matcher(template).replaceAll("$1$2");
        return doubleNewLines.matcher(template).replaceAll("\n\n");
    }
}
