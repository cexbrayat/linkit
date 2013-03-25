package controllers.admin;

import controllers.Check;
import controllers.SecureLinkIt;
import models.ConferenceEvent;
import models.Role;
import models.Talk;
import models.planning.PlanedSlot;
import models.planning.Slot;
import play.mvc.Controller;
import play.mvc.With;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Check({Role.ADMIN_PLANNING, Role.ADMIN_SESSION})
@With(SecureLinkIt.class)
public class AdminPlanning extends Controller {

    public static void index() {
        Map<Slot, Talk> planning = PlanedSlot.on(ConferenceEvent.CURRENT);
        List<Talk> talks = Talk.findAllValidatedOn(ConferenceEvent.CURRENT);
        render(planning, talks);
    }

    public static void save(List<Slot> slots, List<Long> talkIds) {
        if (slots == null) badRequest();
        if (talkIds == null) badRequest();
        if (slots.size() != talkIds.size()) badRequest();

        Map<Slot, Talk> planning = new EnumMap<Slot, Talk>(Slot.class);
        for (int i = 0; i < slots.size(); ++i) {
            if (talkIds.get(i) >= 0) {
                planning.put(slots.get(i), Talk.<Talk>findById(talkIds.get(i)));
            }
        }

        PlanedSlot.save(planning);

        index();
    }
}
