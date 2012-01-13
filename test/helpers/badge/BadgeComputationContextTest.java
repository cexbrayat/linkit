package helpers.badge;

import models.BaseDataUnitTest;
import models.Staff;
import models.Talk;
import org.junit.Test;

/**
 * Unit tests for {@link helpers.badge.BadgeComputationContext}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class BadgeComputationContextTest extends BaseDataUnitTest {
    
    @Test
    public void getNbStaff() {
        final Long actualNbStaff = new BadgeComputationContext().getNbStaff();
        assertNotNull(actualNbStaff);
        assertEquals(Staff.count(), actualNbStaff.longValue());
    }
    
    @Test
    public void getNbSpeaker() {
        final Long actualNbSpeaker = new BadgeComputationContext().getNbSpeakers();
        assertNotNull(actualNbSpeaker);
        assertEquals(Talk.countSpeakers(), actualNbSpeaker.longValue());
    }
}
