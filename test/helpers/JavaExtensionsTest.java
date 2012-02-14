package helpers;

import org.joda.time.DateTime;
import org.junit.Test;
import play.test.UnitTest;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class JavaExtensionsTest extends UnitTest {
    
    @Test public void format() {
        DateTime date = new DateTime();
        date = date.withHourOfDay(15);
        date = date.withMinuteOfHour(23);
        assertEquals("15H23", JavaExtensions.format(date.toDate(), "HH'H'mm"));
    }
}
