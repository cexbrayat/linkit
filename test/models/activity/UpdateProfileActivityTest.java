package models.activity;

import org.junit.*;

/**
 * Unit tests for {@link UpdateProfileActivity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class UpdateProfileActivityTest extends AbstractActivityTest {

    @Test
    public void updateProfile() {
        
        // Non activity for member
        assertNull(Activity.find("select a from Activity a where a.member = ?", member).first());
        
        member.shortDescription = "Nouvelle description";
        member.updateProfile(true);
        
        // One activity for member
        Activity a = Activity.find("select a from Activity a where a.member = ?", member).first();
        assertActivity(a);
        assertTrue(a instanceof UpdateProfileActivity);
        UpdateProfileActivity upa = (UpdateProfileActivity) a;
        assertEquals(member, upa.member);
    }
}
