package models.planning;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import models.Session;

/**
 * The general Mix-IT's planing : for each {@link Slot}, a set of planed {@link Session}s
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class GeneralPlanning extends Planning {

    @OneToMany(mappedBy="planning", cascade = CascadeType.ALL)
    @MapKey(name="session")
    Map<Session, PlanedSlot> planedSessions = new HashMap<Session, PlanedSlot>();

    /**
     * Plans given session on given slot.
     * @param slot
     * @param session
     */
    public void addPlan(Slot slot, Session session) {
        PlanedSlot plan = new PlanedSlot(this, slot, session);
        PlanedSlot previousPlan = planedSessions.put(session, plan);
        if (previousPlan != null) {
            previousPlan.delete();
        }
    }
    
    public Set<Session> getPlan(final Slot slot) {
        Set<Session> sessions = Collections.emptySet();
        
        Iterable<PlanedSlot> plansForSlot = Iterables.filter(planedSessions.values(), new Predicate<PlanedSlot>() {
            public boolean apply(PlanedSlot t) {
                return t.slot == slot;
            }
        });
        sessions = Sets.newHashSet(Iterables.transform(plansForSlot, SESSION_FILTER));

        return sessions;
    }
    
    @Override
    public Set<Session> getPlannedSessions() {
        return planedSessions.keySet();
    }

    /**
     * @return The only general planning existing in DB
     */
    public static GeneralPlanning findUnique() {
        return all().first();
    }
}
