package controllers.admin;

import com.google.common.base.Joiner;
import controllers.Check;
import controllers.SecureLinkIt;
import models.ConferenceEvent;
import models.Role;
import models.Talk;
import models.planning.PlanedSlot;
import models.planning.Slot;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.With;

import java.util.ArrayList;
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

        List<String> errors = new ArrayList<String>();
        Map<Slot, Talk> planning = new EnumMap<Slot, Talk>(Slot.class);
        for (int i = 0; i < slots.size(); ++i) {
            if (talkIds.get(i) >= 0) {

                final Talk talk = Talk.findById(talkIds.get(i));
                if (talk == null) {
                    errors.add("Talk id " + talkIds.get(i) + " unknoww");
                } else if (planning.containsValue(talk)) {
                    errors.add("Talk " + talk + " planifié en double");
                }

                if (planning.containsKey(slots.get(i))) {
                    errors.add("Slot " + slots.get(i) + " déjà affecté");
                }

                planning.put(slots.get(i), Talk.<Talk>findById(talkIds.get(i)));
            }
        }

        if (!errors.isEmpty()) {
            error(Http.StatusCode.BAD_REQUEST, Joiner.on(", ").join(errors));
        }

        PlanedSlot.save(planning);
        index();
    }
}
