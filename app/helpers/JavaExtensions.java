package helpers;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public final class JavaExtensions extends play.templates.JavaExtensions {
    
    public static String escapeHtmlAttr(String value) {
        String result = value;
        result = StringUtils.replace(result, "'", "&#39;");
        result = StringUtils.replace(result, "\"", "&quot;");
        return result;
    }
}
