package hr.antivo.extractwebpagedata.util;

/**
 * Created by antivo on 3/16/15.
 */
public class StringUtil {
    static public String cleanText(String ss) {
        return ss.replaceAll("[\\t\\n\\r]"," ");
    }
}
