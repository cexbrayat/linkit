package models.mailing;

import models.BaseDataUnitTest;
import models.ConferenceEvent;
import models.Sponsor;
import org.junit.Test;

/**
 * Unit tests for {@link MembersSetQuerySponsors}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class MembersSetQuerySponsorsSet extends BaseDataUnitTest {
    
    private MembersSetQuery query = MembersSetQueryFactory.create(MembersSet.Sponsors);
    
    @Test public void find() {
        assertEquals(Sponsor.findOn(ConferenceEvent.CURRENT), query.find());
    }
}
