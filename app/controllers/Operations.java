package controllers;

import models.Role;
import play.Play;
import play.mvc.With;

/**
 *
 * @author Agnes <agnes.crepet@gmail.com>
 */
@Check(Role.ADMIN_MEMBER)
@With(SecureLinkIt.class)
public class Operations extends PageController {

    static final int JOBS_DELAY_AFTER_UPDATE = Integer.valueOf(Play.configuration.getProperty("linkit.job.delayAfterMemberUpdate", "2"));
 
    public static void index() {
        render();
    }
    
    
    public static void syncTicketing() {
        new JobFetchRegisteredTicketingForAllUsers().in(JOBS_DELAY_AFTER_UPDATE);
        index();
    }
   
}
