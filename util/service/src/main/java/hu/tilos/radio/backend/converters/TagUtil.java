package hu.tilos.radio.backend.converters;


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

}
