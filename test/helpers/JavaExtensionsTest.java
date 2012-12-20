package helpers;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import play.Logger;
import play.test.UnitTest;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class JavaExtensionsTest extends UnitTest {
    
    @Test public void format() {
        // Date in GMT
        final DateTimeZone GMT = DateTimeZone.forID("GMT");
        final DateTime date = new DateTime(GMT).withHourOfDay(15).withMinuteOfHour(23);
        Logger.info("date GMT = %s", date.toString());
        
        // target timezone : Paris
        final String timezone = "Europe/Paris";
        Logger.info("date Paris = %s", date.withZone(DateTimeZone.forID(timezone)).toString());
        assertEquals(date.withZone(DateTimeZone.forID(timezone)).toString("HH'H'mm"), JavaExtensions.format(date.toDate(), "HH'H'mm", "FR", timezone));
    }
    
    @Test public void sanitizeHtml() {
        final String dangerous = "<script language='javascript'>"
                + "function yes(){"
                + " document.location.href='http://www.jrebirth.org';"
                + "}"
                + "</script>"
                + "<div onmouseover='yes()'>"
                + "Survolez!"
                + "</div>";
        assertEquals("<div>Survolez!</div>", JavaExtensions.sanitizeHtml(dangerous));
    }
    
    @Test public void sanitizeHtmlSafe() {
        final String safe = "**Coucou** les \"gens\"!";
        assertEquals(safe, JavaExtensions.sanitizeHtml(safe));
    }
    
    @Test public void sanitizeHtmlNull() {
        assertNull(JavaExtensions.sanitizeHtml(null));
    }
    
    @Test public void sanitizeHtmlEmpty() {
        assertEquals("", JavaExtensions.sanitizeHtml(""));
    }
}
