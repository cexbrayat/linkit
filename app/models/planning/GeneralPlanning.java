package models.planning;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import models.Session;

/**
 * The general Mix-IT's planing : for each {@link Slot}, a set of planed {@link Session}s
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class GeneralPlanning extends Planning {
    
    @OneToMany(mappedBy="planning", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Set<PlanedSlot> planedSlots = new HashSet<PlanedSlot>();

    /**
     * Plans given session on given slot.
     * @param slot
     * @param session
     */
    public Session addPlan(Slot slot, Session session) {
        PlanedSlot plan = new PlanedSlot(this, slot, session);
        this.planedSlots.add(plan);
        return null;
    }
    
    public Set<Session> getPlan(final Slot slot) {
        Set<Session> sessions = Collections.emptySet();
        
        Iterable<PlanedSlot> plansForSlot = Iterables.filter(planedSlots, new Predicate<PlanedSlot>() {
            public boolean apply(PlanedSlot t) {
                return t.slot == slot;
            }
        });
        sessions = Sets.newHashSet(Iterables.transform(plansForSlot, SESSION_FILTER));

        return sessions;
    }
    
    @Override
    public Set<Session> getPlannedSessions() {
        return Sets.newHashSet(Iterables.transform(planedSlots, SESSION_FILTER));
    }

    /**
     * @return The only general planning existing in DB
     */
    public static GeneralPlanning findUnique() {
        return all().first();
    }
}
