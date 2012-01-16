import models.BaseDataUnitTest;
import models.Member;
import models.activity.Activity;
import models.activity.SignUpActivity;
import models.auth.LinkItAccount;
import controllers.Security;

import org.junit.*;

public class SecurityTest extends BaseDataUnitTest {
    
    @Test
    public void connect() {
        assertFalse(Security.authenticate("bob", "bob"));
        assertFalse(Security.authenticate("ced", "bob"));
        assertTrue(Security.authenticate("bob", "secret"));
    }
    
    @Test
    public void signupAndConnect() {
        
        final String login = "signupAndConnectTest";
        final String password = "password";
        // Unknown user
        Member nouveau = Member.findByLogin(login);
        assertNull(nouveau);
        
        nouveau = new Member(login);
        nouveau.preregister(new LinkItAccount(password));
        nouveau.register();
        
        assertFalse(Security.authenticate(login, "foo"));
        assertTrue(Security.authenticate(login,password));
    }
}
