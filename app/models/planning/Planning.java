package models.planning;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import models.Talk;

import java.util.*;

public class Planning {

    private List<PlanedSlot> slots = new ArrayList<PlanedSlot>();
    private Map<Slot, Talk> talkPerSlot = new EnumMap<Slot, Talk>(Slot.class);
    private List<Talk> talks = new ArrayList<Talk>();

    void addSlot(PlanedSlot slot) {
        slots.add(slot);
        talkPerSlot.put(slot.slot, slot.talk);
        talks.add(slot.talk);
    }

    void addSlots(Collection<PlanedSlot> slots) {
        for (PlanedSlot slot : slots) {
            addSlot(slot);
        }
    }

    void addTalk(Talk talk) {
        if (!talks.contains(talk)) {
            slots.add(new PlanedSlot(talk));
            talks.add(talk);
        }
    }

    void addTalks(Collection<Talk> talks) {
        for (Talk talk : talks) {
            addTalk(talk);
        }
    }

    public List<PlanedSlot> getSlots() {
        return Collections.unmodifiableList(slots);
    }

    public List<Talk> getTalks() {
        return Collections.unmodifiableList(talks);
    }

    /**
     * @param slot searched slot
     * @return Talk on slot, or null if talk not planned.
     */
    public Talk getTalkOn(Slot slot) {
        return talkPerSlot.get(slot);
    }

    public Talk getTalkById(final Long id) {
        return Iterables.find(talks, new Predicate<Talk>() {
            @Override
            public boolean apply(Talk talk) {
                return id.equals(talk.id);
            }
        }, null);
    }
}
