package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

/**
 * Unit tests for {@link Staff}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class StaffTest extends UnitTest {

    @Before
    public void setUp() {
        Fixtures.deleteAllModels();
        Fixtures.loadModels("data.yml");
    }

    @After
    public void tearDown() {
        Fixtures.deleteAllModels();
    }
    
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
