package models.validation;

import org.junit.Before;
import org.junit.Test;
import play.data.validation.CheckWith;
import play.data.validation.Validation;
import play.test.UnitTest;

/**
 * Unit test for {@link TimeZoneCheck}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class TimeZoneCheckTest extends UnitTest {
    
    private Validation validation;
    static class DomainObject {
        @CheckWith(TimeZoneCheck.class)
        private String timezone;

        public DomainObject(String tz) {
            this.timezone = tz;
        }
        
    }
    
    @Before public void setUp() {
        validation = Validation.current();
        validation.clear();
    }

    @Test public void checkNull() {
        validation.valid(new DomainObject(null));
        assertFalse(validation.hasErrors());
    }
    
    @Test public void checkEmpty() {
        validation.valid(new DomainObject(""));
        assertTrue(validation.hasErrors());
    }
    
    @Test public void checkBlank() {
        validation.valid(new DomainObject("   "));
        assertTrue(validation.hasErrors());
    }

    @Test public void checkOKParis() {
        validation.valid(new DomainObject("Europe/Paris"));
        assertFalse(validation.hasErrors());
    }

    @Test public void checkKO() {
        validation.valid(new DomainObject("efzef"));
        assertTrue(validation.hasErrors());
    }
}
