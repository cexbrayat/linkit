package models.mailing;

import models.BaseDataUnitTest;
import models.Staff;
import org.junit.Test;

/**
 * Unit tests for {@link MembersSetQueryStaff}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class MembersSetQueryStaffTest extends BaseDataUnitTest {
    
    private MembersSetQuery query = MembersSetQueryFactory.create(MembersSet.Staff);
    
    @Test public void find() {
        assertEquals(Staff.findAll(), query.find());
    }
}
