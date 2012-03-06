package models.mailing;

import models.BaseDataUnitTest;
import models.Sponsor;
import org.junit.Test;

/**
 * Unit tests for {@link MembersSetQuerySponsors}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class MembersSetQuerySponsorsSet extends BaseDataUnitTest {
    
    private MembersSetQuerySponsors query = new MembersSetQuerySponsors();
    
    @Test public void find() {
        assertEquals(Sponsor.findAll(), query.find());
    }
}
