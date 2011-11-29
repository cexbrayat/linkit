package models.activity;

import models.auth.LinkItAccount;
import models.Member;
import org.junit.*;

/**
 * Unit tests for {@link SignUpActivity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class SignUpActivityTest extends AbstractActivityTest {

    @Test
    public void signup() {
        
        // Unknown user
        Member nouveau = Member.findByLogin("new");
        assertNull(nouveau);
        
        nouveau = new Member("new").register(new LinkItAccount("password"));
        
        // One activity for nouveau
        Activity a = Activity.find("select a from Activity a where a.member = ?", nouveau).first();
        assertActivity(a);
        assertTrue(a instanceof SignUpActivity);
        SignUpActivity sua = (SignUpActivity) a;
        assertEquals(nouveau, sua.member);
    }
}
