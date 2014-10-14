package hu.tilos.radio.backend.converters;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility to process @tag and #tags.
 */
public class TagUtil {

    public static final String GENERIC_SIMPLE = "(?<![&#])#([\\w&;áíűőüöúóéÁÍŰŐÜÖÚÓÉ-]+)";

    public static final String GENERIC_COMPLEX = "\\#\\{(.+?)\\}";

    public static final String PERSON_SIMPLE = "(?<![\\w@])\\@([\\w&;áíűőüöúóéÁÍŰŐÜÖÚÓÉ-]+)";

    public static final String PERSON_COMPLEX = "(?<![\\w@])\\@\\{(.+?)\\}";

    public String replaceToHtml(String source) {
        source = source.replaceAll(GENERIC_SIMPLE, "<a href=\"/tag/$1\"><span class=\"label label-primary\">$1</span></a>");
        source = source.replaceAll(GENERIC_COMPLEX, "<a href=\"/tag/$1\"><span class=\"label label-primary\">$1</span></a>");
        source = source.replaceAll(PERSON_SIMPLE, "<a href=\"/tag/$1\"><span class=\"label label-success\">$1</span></a>");
        source = source.replaceAll(PERSON_COMPLEX, "<a href=\"/tag/$1\"><span class=\"label label-success\">$1</span></a>");
        source = source.replace("@@", "@");
        source = source.replace("##", "#");
        return source;
    }

    public Set<String> getTags(String text) {
        Matcher m = Pattern.compile(GENERIC_SIMPLE).matcher(text);
        Set<String> tags = new HashSet();
        while (m.find()) {
            tags.add(m.group().substring(1));
        }
        return tags;
    }

}
