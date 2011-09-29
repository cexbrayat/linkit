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
        final int originalNbBadges = staffMember.badges.size();
        
        staffMember.addBadge(Badge.Sponsor);
        staffMember.save();
        
        staffMember = Staff.findByLogin(login);
        assertEquals(originalNbBadges+1, staffMember.badges.size());
        
        // Adding same badge twice : no consequences
        staffMember.addBadge(Badge.Sponsor);
        staffMember.save();
        staffMember = Staff.findByLogin(login);
        assertEquals(originalNbBadges+1, staffMember.badges.size());
    }
    
}
