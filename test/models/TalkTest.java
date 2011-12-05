package models;

import org.junit.*;

/**
 * Unit tests for {@link Session} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 * @author Agnes <agnes.crepet@gmail.com>
 */
public class TalkTest extends BaseDataUnitTest {

    @Test public void recents() {
        assertNotNull(Talk.recents(1, 10));
    }
}
