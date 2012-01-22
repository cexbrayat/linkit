package models;

import java.util.ArrayList;
import java.util.Collections;
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
    public void findAllOrdered() {
        List<Interest> all = Interest.findAllOrdered();
        assertNotNull(all);
        // Vérifier le tri par ordre alphabétique
        List<Interest> sortedAll = new ArrayList<Interest>(all);
        Collections.sort(sortedAll);
        assertEquals(sortedAll, all);
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

    @Test
    public void deleteByName() {
        Member bob = Member.findByLogin("bob");
        Member ced = Member.findByLogin("ced");

        // Add interest now
        ced.addInterest("Java").addInterest("Hadoop").save();
        bob.addInterest("TDD").addInterest("Java").save();


        // Delete Java Interest
        Interest.deleteByName("Java");
        assertEquals(1, ced.interests.size());
        assertEquals(1, bob.interests.size());
        // Check Interests Cloud
        // Be careful to the alphabetical order!
        List<Map> cloud = Interest.getCloud();
        assertEquals(
                "[{interest=Hadoop, pound=1}, {interest=TDD, pound=1}]",
                cloud.toString());
    }

    @Test
    public void merge() {
        Member bob = Member.findByLogin("bob");
        Member ced = Member.findByLogin("ced");

        // Add interest now
        ced.addInterest("Java").addInterest("Hadoop").save();
        bob.addInterest("TDD").addInterest("java").save();


        // Merge Java Interest and java Interest : keep Java
        Interest interestToDeleted = Interest.findByName("java");
        interestToDeleted.merge("Java");
        assertEquals(2, ced.interests.size());
        assertEquals(2, bob.interests.size());
        // Check Interests Cloud
        // Be careful to the alphabetical order!
        List<Map> cloud = Interest.getCloud();
        assertEquals(
                "[{interest=Hadoop, pound=1}, {interest=Java, pound=2}, {interest=TDD, pound=1}]",
                cloud.toString());
    }
}
