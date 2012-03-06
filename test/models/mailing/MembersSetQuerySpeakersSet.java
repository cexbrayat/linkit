package models.mailing;

import models.BaseDataUnitTest;
import models.Talk;
import org.junit.Test;

/**
 * Unit tests for {@link MembersSetQuerySpeakers}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class MembersSetQuerySpeakersSet extends BaseDataUnitTest {
    
    private MembersSetQuerySpeakers query = new MembersSetQuerySpeakers();
    
    @Test public void find() {
        assertEquals(Talk.findAllSpeakers(), query.find());
    }
}
