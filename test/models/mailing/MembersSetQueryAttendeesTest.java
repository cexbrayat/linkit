package models.mailing;

import models.BaseDataUnitTest;
import models.Member;
import org.junit.Test;

/**
 * Unit tests for {@link MembersSetQueryAttendees}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class MembersSetQueryAttendeesTest extends BaseDataUnitTest {
    
    private MembersSetQuery query = MembersSetQueryFactory.create(MembersSet.Attendees);
    
    @Test public void find() {
        assertEquals(Member.find("ticketingRegistered=true").fetch(), query.find());
    }
}
