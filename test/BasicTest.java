import controllers.Security;

import org.junit.*;
import play.test.*;

public class BasicTest extends UnitTest {

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
    public void connect() {
        assertFalse(Security.authenticate("bob", "bob"));
        assertFalse(Security.authenticate("ced", "bob"));
        assertTrue(Security.authenticate("bob", "secret"));
    }
}
