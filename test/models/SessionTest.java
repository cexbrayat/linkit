package models;

import org.h2.util.StringUtils;
import org.junit.*;
import play.test.*;

/**
 * Unit tests for {@link Session} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 * @author Agnes <agnes.crepet@gmail.com>
 */
public class SessionTest extends UnitTest {

    @Before
    public void setUp() {
        Fixtures.deleteAllModels();
        Fixtures.loadModels("data.yml");
    }

    @After
    public void tearDown() {
        Fixtures.deleteAllModels();
    }
    
    @Test
    public void saveWithBigDescription() {
        Session session = new Session();
        String description = StringUtils.pad("testwith4000char", 4000, "a" , true);
        session.description = description;
        session.save();
    }
    
        @Test
    public void testInterests() {
        Session session1 = new Session();
        Session session2 = new Session();

        // Well
        assertEquals(0, Session.findSessionsLinkedWith("Java").size());

        // Add interest now
        session1.addInterest("Java").addInterest("TDD").addInterest("Hadoop").save();
        session2.addInterest("Java").save();

        // Simple Check
        assertEquals(2, Session.findSessionsLinkedWith("Java").size());
        assertEquals(1, Session.findSessionsLinkedWith("TDD").size());
        assertEquals(1, Session.findSessionsLinkedWith("Hadoop").size());

    }
}
