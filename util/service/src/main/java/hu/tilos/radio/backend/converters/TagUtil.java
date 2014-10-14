package hu.tilos.radio.backend.converters;


import hu.radio.tilos.model.Tag;
import hu.radio.tilos.model.type.TagType;

import java.util.*;
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

    private Map<TagType, List<Pattern>> patterns = new HashMap<>();

    public TagUtil() {
        patterns.put(TagType.GENERIC, new ArrayList<Pattern>());
        patterns.put(TagType.PERSON, new ArrayList<Pattern>());

        patterns.get(TagType.GENERIC).add(Pattern.compile(GENERIC_SIMPLE));
        patterns.get(TagType.GENERIC).add(Pattern.compile(GENERIC_COMPLEX));
        patterns.get(TagType.PERSON).add(Pattern.compile(PERSON_SIMPLE));
        patterns.get(TagType.PERSON).add(Pattern.compile(PERSON_COMPLEX));
    }

    public String replaceToHtml(String source) {
        source = source.replaceAll(GENERIC_SIMPLE, "<a href=\"/tag/$1\"><span class=\"label label-primary\">$1</span></a>");
        source = source.replaceAll(GENERIC_COMPLEX, "<a href=\"/tag/$1\"><span class=\"label label-primary\">$1</span></a>");
        source = source.replaceAll(PERSON_SIMPLE, "<a href=\"/tag/$1\"><span class=\"label label-success\">$1</span></a>");
        source = source.replaceAll(PERSON_COMPLEX, "<a href=\"/tag/$1\"><span class=\"label label-success\">$1</span></a>");
        source = source.replace("@@", "@");
        source = source.replace("##", "#");
        return source;
    }

    public Set<Tag> getTags(String text) {
        Set<Tag> tags = new HashSet<>();
        for (TagType type : patterns.keySet()) {
            for (Pattern p : patterns.get(type)) {
                Matcher m = p.matcher(text);
                while (m.find()) {
                    Tag t = new Tag();
                    t.setName(m.group().substring(1));
                    t.setType(type);
                    tags.add(t);
                }
            }

        }
        return tags;
    }

}
