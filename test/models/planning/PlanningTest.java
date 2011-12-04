package models.planning;

import models.BaseDataUnitTest;
import models.Member;
import models.Session;
import models.Talk;
import org.junit.Test;

/**
 * Unit tests for {@link Planning}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class PlanningTest extends BaseDataUnitTest {
    
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
        
        Planning p = new Planning(member);
        p.addPlan(Slot.NineTen, s1);
        p.addPlan(Slot.TenEleven, s2);
        p.save();
                
        p = Planning.findById(p.id);
        assertSame(member, p.member);
        assertSame(s1, p.getPlan(Slot.NineTen));
        assertSame(s2, p.getPlan(Slot.TenEleven));
        assertNull(p.getPlan(Slot.ElevenNoon));
        
        // Replanning : s1 was the previous session planned
        assertSame(s1, p.addPlan(Slot.NineTen, s3));
        p.save();
        p = Planning.findById(p.id);
        assertSame(s3, p.getPlan(Slot.NineTen));
    }
}
