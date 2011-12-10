package models;

import java.util.List;
import java.util.Map;
import org.junit.*;

/**
 * Unit tests for {@link Interest} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class InterestTest extends BaseDataUnitTest {

    private static Interest createInterest(String name) {
        return new Interest(name).save();
    }
    
    @Test
    public void findByName() {
        final String name = "toto";
        assertNull(Interest.findByName(name));
        createInterest(name);
        assertNotNull(Interest.findByName(name));
    }
    
    @Test
    public void findOrCreateByName() {
        final String name = "toto";
        assertNull(Interest.find("byName", name).first());
        createInterest(name);
        assertNotNull(Interest.findByName(name));
    }
    
    @Test
    public void getCloud() {
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
