package models;

import java.util.List;
import java.util.Map;
import org.junit.*;

/**
 * Unit tests for {@link Interest} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class InterestTest extends BaseDataUnitTest {

    @Test
    public void testFindMembersInterestedIn() {
        Member bob = Member.findByLogin("bob");
        Member ced = Member.findByLogin("ced");

        // Add interest now
        ced.addInterest("Java").addInterest("Hadoop").save();
        bob.addInterest("TDD").addInterest("Java").save();


        // Check Interests Cloud
        // Be careful to the alphabetical order!
        List<Map> cloud = Interest.getCloud();
        assertEquals(
                "[{interest=Hadoop, pound=1}, {interest=Java, pound=2}, {interest=TDD, pound=1}]",
                cloud.toString());
    }
}
