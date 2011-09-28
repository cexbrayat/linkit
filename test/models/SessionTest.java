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
}
