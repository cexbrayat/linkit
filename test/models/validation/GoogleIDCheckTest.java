package models.validation;

import org.junit.Before;
import org.junit.Test;
import play.data.validation.CheckWith;
import play.data.validation.Validation;
import play.test.UnitTest;

/**
 * Unit test for {@link GoogleIDCheck}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class GoogleIDCheckTest extends UnitTest {
    
    private Validation validation;
    static class DomainObject {
        @CheckWith(GoogleIDCheck.class)
        private String googleId;

        public DomainObject(String googleId) {
            this.googleId = googleId;
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
        assertFalse(validation.hasErrors());
    }
    
    @Test public void checkBlank() {
        validation.valid(new DomainObject("   "));
        assertTrue(validation.hasErrors());
    }

    @Test public void checkOKNumber() {
        validation.valid(new DomainObject("114128610730314333831"));
        assertFalse(validation.hasErrors());
    }

    @Test public void checkKONotANumberWithoutPlus() {
        validation.valid(new DomainObject("1a4128610730314333831"));
        assertTrue(validation.hasErrors());
    }

    @Test public void checkOKVanityName() {
        validation.valid(new DomainObject("+LaurentVictorino"));
        assertFalse(validation.hasErrors());
    }

    @Test public void checkKONameWhitespaces() {
        validation.valid(new DomainObject("+Laurent Victorino"));
        assertTrue(validation.hasErrors());
    }

    @Test public void checkKONumberTooShort() {
        validation.valid(new DomainObject("99999999999999999999"));
        assertTrue(validation.hasErrors());
    }
    
    @Test public void checkKONumberTooLong() {
        validation.valid(new DomainObject("1000000000000000000000"));
        assertTrue(validation.hasErrors());
    }
}
