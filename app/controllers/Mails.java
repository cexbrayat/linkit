package controllers;

import java.util.List;
import models.Session;
import models.Staff;
import play.Play;
import play.mvc.Mailer;

/**
 * Controlleur d'envoi de mails de notification
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class Mails extends Mailer {

    private static final String FROM = Play.configuration.getProperty("linkit.mail.from");
    private static final String TO = Play.configuration.getProperty("linkit.mail.to");
    
    public static void updateSession(Session talk) {
        setSubject("Notification Link-IT : session \""+talk+"\" mise à jour");
        setFrom(FROM);
        addStaffRecipients();
        send(talk);
    }
    
    private static void addStaffRecipients() {
        final List<Staff> members = Staff.findAll();
        for (Staff staff : members) {
            addRecipient(staff.email);
        }
    }
}
