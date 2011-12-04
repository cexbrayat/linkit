package models;

import org.junit.Test;

/**
 * Unit tests for {@link Staff}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class StaffTest extends BaseDataUnitTest {

    @Test public void load() {
        final String login = "ced";
        
        Staff staffMember = Staff.findByLogin(login);
        assertNotNull(staffMember);
        
        assertNotNull(staffMember.badges);
        assertTrue(staffMember.badges.contains(Badge.Staff));
    }

    @Test public void create() {
        Staff s = new Staff("toto");
        assertNotNull(s.badges);
        assertTrue(s.badges.contains(Badge.Staff));
        s.save();
    }
}
