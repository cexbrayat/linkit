package models;

import org.junit.Test;

/**
 * Unit tests for {@link AccountTest}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class AccountTest extends BaseDataUnitTest {
    
    @Test public void findAllIds() {
        assertNotNull(Account.findAllIds());
    }
}
