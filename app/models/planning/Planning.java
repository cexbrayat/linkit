package models.planning;

import java.util.EnumMap;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import models.Member;
import models.Session;
import play.db.jpa.Model;

/**
 * A {@link Member}'s planing : he may choose a {@link Session} per {@link Slot} 
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class Planning extends Model {
    
    @ManyToOne(optional = false)
    public Member member;

    @OneToMany(mappedBy="planning", cascade = CascadeType.ALL)
    @MapKey(name="slot")
    Map<Slot, PlanedSlot> planedSlots = new EnumMap<Slot, PlanedSlot>(Slot.class);

    public Planning(Member member) {
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
        
        PlanedSlot plan = new PlanedSlot(slot, session);
        plan.planning = this;
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
}
