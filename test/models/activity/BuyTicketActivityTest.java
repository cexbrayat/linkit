package models.activity;

import models.Staff;
import org.junit.*;

/**
 * Unit tests for {@link BuyTicketActivity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class BuyTicketActivityTest extends AbstractActivityTest {

    @Test
    public void ticketingRegistered() {
        // Non activity for member
        assertNull(Activity.find("select a from Activity a where a.member = ?", member).first());

        member.setTicketingRegistered(false);
        member.save();
        // Still no activity for member
        assertNull(Activity.find("select a from Activity a where a.member = ?", member).first());

        member.setTicketingRegistered(true);
        member.save();
        
        // One activity for member
        assertEquals(1l, Activity.count("from Activity a where a.member = ?", member));
        Activity a = Activity.find("select a from Activity a where a.member = ?", member).first();
        assertActivity(a);
        assertTrue(a instanceof BuyTicketActivity);
        BuyTicketActivity bta = (BuyTicketActivity) a;
        assertEquals(member, bta.member);

        member.setTicketingRegistered(true);
        member.save();
        // Still only one activity for member
        assertEquals(1l, Activity.count("from Activity a where a.member = ?", member));
    }

    @Test
    public void ticketingRegisteredStaff() {
        Staff staff = Staff.all().first();
        // Non activity for staff
        assertNull(Activity.find("select a from Activity a where a.member = ?", staff).first());

        staff.setTicketingRegistered(false);
        staff.save();
        // Still no activity for staff
        assertNull(Activity.find("select a from Activity a where a.member = ?", staff).first());

        staff.setTicketingRegistered(true);
        staff.save();
        
        // Still no activity for staf
        assertNull(Activity.find("select a from Activity a where a.member = ?", staff).first());
    }
}
