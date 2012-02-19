package models;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
    public void findByNameSimple() {
        final String name = "toto";
        assertNull(Interest.findByName(name));
        createInterest(name);
        assertNotNull(Interest.findByName(name));
    }

    @Test
    public void findByNameDifferentsCases() {
        final Interest interest = createInterest("Toto");
        assertSame(interest, Interest.findByName("Toto"));
        assertSame(interest, Interest.findByName("toto"));
        assertSame(interest, Interest.findByName("TOTO"));
        assertSame(interest, Interest.findByName(" Toto "));
        assertSame(interest, Interest.findByName("   TOTO"));
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
        assertTrue(cloud.contains(buildCloudMap(Interest.findByName("Java"),2L)));
        assertTrue(cloud.contains(buildCloudMap(Interest.findByName("Hadoop"),1L)));
        assertTrue(cloud.contains(buildCloudMap(Interest.findByName("TDD"),1L)));
    }

    @Test
    public void delete() {
        Member bob = Member.findByLogin("bob");
        Member ced = Member.findByLogin("ced");

        // Add interest now
        ced.addInterest("Java").addInterest("Hadoop").save();
        bob.addInterest("TDD").addInterest("Java").save();


        // Delete Java Interest
        Interest.findByName("Java").delete();
        assertEquals(1, ced.interests.size());
        assertEquals(1, bob.interests.size());
        assertEquals(Sets.newHashSet(Interest.findByName("hadoop")), ced.interests);
        assertEquals(Sets.newHashSet(Interest.findByName("TDD")), bob.interests);
    }

    @Test
    public void merge() {
        Member bob = Member.findByLogin("bob");
        Member ced = Member.findByLogin("ced");

        // Add interest now
        ced.addInterest("java6").addInterest("Hadoop").save();
        bob.addInterest("TDD").addInterest("java").save();


        // Merge Java Interest and java Interest : keep Java
        Interest interestToDeleted = Interest.findByName("java6");
        interestToDeleted.merge(Interest.findByName("java"));

        assertEquals(Sets.newHashSet(Interest.findByName("java"), Interest.findByName("hadoop")), ced.interests);
        assertEquals(Sets.newHashSet(Interest.findByName("TDD"), Interest.findByName("java")), bob.interests);
    }
    
    private Map buildCloudMap(Interest interest, Long pound){
        Map m = new HashMap();
        m.put("interest", interest);
        m.put("pound", pound);
        return m;
    }
}
