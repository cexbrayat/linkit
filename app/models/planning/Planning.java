package models.planning;

import com.google.common.base.Function;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import models.Session;
import play.db.jpa.Model;

/**
 * A planning, collection of {@link PlanedSlot}
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Planning extends Model {
    
    protected static final Function<PlanedSlot, Session> SESSION_FILTER = new Function<PlanedSlot, Session>() {
        public Session apply(PlanedSlot ps) {
            return ps.session;
        }
    };
    
    /**
     * Plans given slot for given session
     * @param slot
     * @param s
     * @return Potential previous session planed, if planning accept only a single session per slot. May be null
     */
    public abstract Session addPlan(final Slot slot, final Session s);
    
    /**
     * @return already planned session
     */
    public abstract Set<Session> getPlannedSessions();
}
