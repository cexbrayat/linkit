package helpers;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.owasp.validator.html.*;
import play.Logger;
import play.i18n.Lang;
import play.i18n.Messages;
import play.vfs.VirtualFile;

/**
 * @author Sryl <cyril.lacote@gmail.com>
 */
public final class JavaExtensions extends play.templates.JavaExtensions {
    
    private static Policy POLICY;
    
    private static Policy getPolicy() {
        if (POLICY == null) {
            try {
                POLICY = Policy.getInstance(VirtualFile.fromRelativePath("conf/antisamy-myspace-1.4.4.xml").getRealFile());
            } catch (PolicyException pe) {
                Logger.error(pe, "Error while loading sanitizing policy config file");
            }
        }
        return POLICY;
    }
    
    public static String sanitizeHtml(String value) {
        
        if (StringUtils.isBlank(value)) return value;
        
        String result = value;
        try {
            AntiSamy as = new AntiSamy();
            CleanResults cr = as.scan(value, getPolicy());
            result = cr.getCleanHTML();
            // AntiSamy convert quotes (") to HTML entities
            result = StringEscapeUtils.unescapeHtml(result);
        } catch (PolicyException pe) {
            Logger.error(pe, "Error while sanitizing : " + value);
        } catch (ScanException se) {
            Logger.error(se, "Error while sanitizing : " + value);
        }
        return result;
    }
    
    public static String escapeHtmlAttr(String value) {
        String result = value;
        result = StringUtils.replace(result, "'", "&#39;");
        result = StringUtils.replace(result, "\"", "&quot;");
        return result;
    }

    public static String i18n(LocalTime time) {
        if (time == null) return null;
        return time.toString(Messages.get("slot.time.format"));
    }

    public static String i18n(LocalDate day) {
        if (day == null) return null;
        return day.toString(Messages.get("slot.date.format"), Lang.getLocale());
    }

    public static String i18n(Enum e) {
        if (e == null) return null;
        return Messages.get("enum."+e.getClass().getSimpleName()+'.'+e.name());
    }
}
