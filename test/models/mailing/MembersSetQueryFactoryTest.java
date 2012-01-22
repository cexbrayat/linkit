package models.mailing;

import org.junit.Test;
import play.test.UnitTest;

/**
 * Unit tests for {@link MembersSetQueryFactory}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class MembersSetQueryFactoryTest extends UnitTest {
    
    @Test public void create() {
        for (MembersSet set : MembersSet.values()) {
            assertNotNull(MembersSetQueryFactory.create(set));
        }
    }
}
