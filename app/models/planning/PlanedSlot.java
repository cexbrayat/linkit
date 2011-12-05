package models.planning;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import models.Session;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * A {@link planning}'s choice of {@link Session} for a given {@link Slot} 
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class PlanedSlot extends Model {
    
    @ManyToOne(optional = false)
    public Planning planning;
    
    @Enumerated(EnumType.STRING)
    @Required
    public Slot slot;
    
    @ManyToOne(optional = false)
    @Required
    public Session session;

    public PlanedSlot(Planning planning, Slot slot, Session session) {
        this.planning = planning;
        this.slot = slot;
        this.session = session;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PlanedSlot other = (PlanedSlot) obj;
        return new EqualsBuilder()
                .append(this.planning, other.planning)
                .append(this.slot, other.slot)
                .append(this.session, other.session)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(planning)
                .append(slot)
                .append(session)
                .toHashCode();
    }   
}
