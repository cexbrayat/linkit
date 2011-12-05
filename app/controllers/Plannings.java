package controllers;

import java.util.List;
import models.Session;
import models.planning.GeneralPlanning;
import models.planning.Planning;
import models.planning.Slot;
import play.mvc.Controller;

/**
 * Manages sessions' planning
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class Plannings extends Controller {
    
    public static void index() {
        GeneralPlanning planning = GeneralPlanning.findUnique();
        if (planning == null) {
            planning = new GeneralPlanning().save();
        }
        // Toutes les sessions
        List<Session> sessions = Session.findAll();
        // Seulement les sessions non planifiées
        sessions.removeAll(planning.getPlannedSessions());
        render(planning, sessions);
    }
    
    public static boolean plan(long planningId, Slot slot, long sessionId) {
        Planning planning = Planning.findById(planningId);
        Session talk = Session.findById(sessionId);
        planning.addPlan(slot, talk);
        planning.save();
        return true;
    }
}
