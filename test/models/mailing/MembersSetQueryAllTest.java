package models.mailing;

import models.BaseDataUnitTest;
import models.Member;
import org.junit.Test;

/**
 * Unit tests for {@link MembersSetQueryAll}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class MembersSetQueryAllTest extends BaseDataUnitTest {
    
    private MembersSetQuery query = MembersSetQueryFactory.create(MembersSet.All);
    
    @Test public void find() {
        assertEquals(Member.findAll(), query.find());
    }
}
