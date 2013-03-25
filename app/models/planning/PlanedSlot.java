package models.planning;

import models.ConferenceEvent;
import models.Talk;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
@Table( uniqueConstraints = {
        // Unique key : only one session per slot
        @UniqueConstraint(name = "PlanedSlot_UK1", columnNames = {"event", "slot"}),
        // Unique key : only one slot per session
        @UniqueConstraint(name = "PlanedSlot_UK2", columnNames = {"event", "talk_id"}),
})
public class PlanedSlot extends Model {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public ConferenceEvent event;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Required
    public Slot slot;
    
    @ManyToOne(optional = false)
    @Required
    public Talk talk;

    /** Technical constructor */
    protected PlanedSlot() {
        this.event = ConferenceEvent.CURRENT;
    }

    public PlanedSlot(Slot slot, Talk talk) {
        this();
        this.slot = slot;
        this.talk = talk;
    }

    public static Map<Slot, Talk> on(ConferenceEvent event) {
        List<PlanedSlot> slots = find("event = ?", event).fetch();
        Map<Slot, Talk> result = new EnumMap<Slot, Talk>(Slot.class);
        for (PlanedSlot ps : slots) {
            result.put(ps.slot, ps.talk);
        }
        return result;
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
                .append(this.slot, other.slot)
                .append(this.talk, other.talk)
                .isEquals();
    }

    public static void save(Map<Slot,Talk> planning) {
        delete("event = ?", ConferenceEvent.CURRENT);
        for (Map.Entry<Slot, Talk> entry : planning.entrySet()) {
            new PlanedSlot(entry.getKey(), entry.getValue()).save();
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(slot)
                .append(talk)
                .toHashCode();
    }
}
