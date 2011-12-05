package models.planning;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import models.Member;
import models.Session;

/**
 * A {@link Member}'s planing : he may choose a {@link Session} per {@link Slot} 
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class MemberPlanning extends Planning {
    
    @ManyToOne
    public Member member;

    @OneToMany(mappedBy="planning", cascade = CascadeType.ALL)
    @MapKey(name="slot")
    Map<Slot, PlanedSlot> planedSlots = new EnumMap<Slot, PlanedSlot>(Slot.class);

    public MemberPlanning(Member member) {
        this.member = member;
    }

    /**
     * Plans given session on given slot.
     * @param slot
     * @param session
     * @return Previous planed Session, if any.
     */
    public Session addPlan(Slot slot, Session session) {
        Session previousSession = null;
        
        PlanedSlot plan = new PlanedSlot(this, slot, session);
        PlanedSlot previousPlan = planedSlots.put(slot, plan);
        if (previousPlan != null) {
            previousSession = previousPlan.session;
            previousPlan.delete();
        }
        return previousSession;
    }
    
    public Session getPlan(Slot slot) {
        Session session = null;
        
        PlanedSlot plan = planedSlots.get(slot);
        if (plan != null) {
            session = plan.session;
        }
        
        return session;
    }

    @Override
    public Set<Session> getPlannedSessions() {
        return Sets.newHashSet(Iterables.transform(planedSlots.values(), SESSION_FILTER));
    }
}
