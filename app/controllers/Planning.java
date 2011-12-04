package controllers;

import java.util.List;
import models.LightningTalk;
import models.Session;
import play.mvc.Controller;

/**
 * Manages sessions planning
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class Planning extends Controller {
    
    public static void index() {
        List<Session> sessions = Session.findAll();
        List<LightningTalk> lightningTalks = LightningTalk.findAll();
        render(sessions, lightningTalks);
    }
}
