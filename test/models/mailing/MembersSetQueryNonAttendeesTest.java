package models.mailing;

import models.BaseDataUnitTest;
import models.Member;
import org.junit.Test;

/**
 * Unit tests for {@link MembersSetQueryNonAttendees}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class MembersSetQueryNonAttendeesTest extends BaseDataUnitTest {
    
    private MembersSetQueryNonAttendees query = new MembersSetQueryNonAttendees();
    
    @Test public void find() {
        assertEquals(Member.find("ticketingRegistered=false").fetch(), query.find());
    }
}
