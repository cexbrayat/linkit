package models.activity;

import models.Member;
import org.junit.*;

/**
 * Unit tests for {@link UpdateProfileActivity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class UpdateProfileActivityTest extends AbstractActivityTest {

    @Test
    public void updateProfile() {
        
        Member bob = Member.findByLogin("bob");
        
        // Non activity for Bob
        assertNull(Activity.find("select a from Activity a where a.member = ?", bob).first());
        
        bob.twitterName = "bob";
        bob.updateProfile();
        
        // One activity for Bob
        Activity a = Activity.find("select a from Activity a where a.member = ?", bob).first();
        assertActivity(a);
        assertTrue(a instanceof UpdateProfileActivity);
        UpdateProfileActivity upa = (UpdateProfileActivity) a;
        assertEquals(bob, upa.member);
    }
}
