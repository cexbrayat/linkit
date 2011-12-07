package models.planning;

import com.google.common.collect.Sets;
import models.BaseDataUnitTest;
import models.Session;
import models.Talk;
import org.junit.Test;

/**
 * Unit tests for {@link MemberPlanning}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class GeneralPlanningTest extends BaseDataUnitTest {
    
    private static Session createSession(String text) {
        return new Talk().save();
    }
    
    @Test
    public void plan() {
        final Session s1 = createSession("s1");
        final Session s2 = createSession("s2");
        final Session s3 = createSession("s3");
        
        GeneralPlanning p = new GeneralPlanning();
        p.addPlan(Slot.NineTen, s1);
        p.addPlan(Slot.NineTen, s2);
        p.addPlan(Slot.ElevenNoon, s3);
        p.save();

        p = GeneralPlanning.findById(p.id);
        assertEquals(Sets.newHashSet(s1, s2), p.getPlan(Slot.NineTen));
        assertEquals(Sets.newHashSet(s3), p.getPlan(Slot.ElevenNoon));
        assertTrue(p.getPlan(Slot.MidDayBreak).isEmpty());

        // Add new plan
        p.addPlan(Slot.ElevenNoon, s1);
        p.addPlan(Slot.ElevenNoon, s1);
        p.save();
        p = GeneralPlanning.findById(p.id);
        assertEquals(Sets.newHashSet(s1, s3), p.getPlan(Slot.ElevenNoon));
        // Just to be sure that there is no duplicated data in DB
        assertEquals(1l, PlanedSlot.count("session = ?", s1));
    }
    
    @Test
    public void getPlannedSessions() {
        final Session s1 = createSession("s1");
        final Session s2 = createSession("s2");
        final Session s3 = createSession("s3");
        
        GeneralPlanning p = new GeneralPlanning();
        p.addPlan(Slot.NineTen, s1);
        p.addPlan(Slot.NineTen, s2);
        p.addPlan(Slot.ElevenNoon, s2);
        p.addPlan(Slot.ElevenNoon, s3);

        assertEquals(Sets.newHashSet(s1, s2, s3), p.getPlannedSessions());
    }
    
    @Test public void findUnique() {
        GeneralPlanning p = GeneralPlanning.findUnique();
        assertNull(p);
        
        p = new GeneralPlanning().save();
        assertSame(p, GeneralPlanning.findUnique());
        assertSame(p, GeneralPlanning.findUnique());
    }
}
