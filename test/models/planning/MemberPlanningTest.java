package models.planning;

import com.google.common.collect.Sets;
import models.BaseDataUnitTest;
import models.Member;
import models.Session;
import models.Talk;
import org.junit.Test;

/**
 * Unit tests for {@link MemberPlanning}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class MemberPlanningTest extends BaseDataUnitTest {
    
    private static Member createMember(String login) {
        return new Member(login).save();
    }
    
    private static Session createSession(String text) {
        return new Talk().save();
    }
    
    @Test
    public void plan() {
        final Member member = createMember("toto");
        final Session s1 = createSession("s1");
        final Session s2 = createSession("s2");
        final Session s3 = createSession("s3");
        
        MemberPlanning p = new MemberPlanning(member);
        p.addPlan(Slot.NineTen, s1);
        p.addPlan(Slot.TenEleven, s2);
        p.save();
                
        p = MemberPlanning.findById(p.id);
        assertSame(member, p.member);
        assertSame(s1, p.getPlan(Slot.NineTen));
        assertSame(s2, p.getPlan(Slot.TenEleven));
        assertNull(p.getPlan(Slot.ElevenNoon));
        
        // Replanning : s1 was the previous session planned
        assertSame(s1, p.addPlan(Slot.NineTen, s3));
        p.save();
        p = MemberPlanning.findById(p.id);
        assertSame(s3, p.getPlan(Slot.NineTen));
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
}
