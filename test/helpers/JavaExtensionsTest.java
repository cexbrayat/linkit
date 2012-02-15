package helpers;

import java.util.TimeZone;
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
        assertEquals("16H23", JavaExtensions.format(date.toDate(), "HH'H'mm", "FR", timezone));
    }
}
