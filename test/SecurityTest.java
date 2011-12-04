import models.BaseDataUnitTest;
import controllers.Security;

import org.junit.*;

public class SecurityTest extends BaseDataUnitTest {
    
    @Test
    public void connect() {
        assertFalse(Security.authenticate("bob", "bob"));
        assertFalse(Security.authenticate("ced", "bob"));
        assertTrue(Security.authenticate("bob", "secret"));
    }
}
