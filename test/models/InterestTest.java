package models;


import java.util.List;
import java.util.Map;
import org.junit.*;
import play.test.*;

/**
 * Unit tests for {@link Interest} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class InterestTest extends UnitTest {

    @Before
    public void setUp() {
        Fixtures.deleteAllModels();
        Fixtures.loadModels("data.yml");
    }

    @After
    public void tearDown() {
        Fixtures.deleteAllModels();
    }

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
