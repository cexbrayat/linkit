package controllers;

import java.util.List;
import models.Email;
import models.Member;
import models.Session;
import models.mailing.MailingStatus;
import models.mailing.MembersSet;
import models.mailing.MembersSetQueryFactory;
import play.Play;
import play.mvc.Mailer;

/**
 * Controlleur d'envoi de mails
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class Mails extends Mailer {

    private static final String FROM = Play.configuration.getProperty("linkit.mail.from");
    
    public static void updateSession(Session talk) {
        setSubject("Notification Link-IT : session \""+talk+"\" mise à jour");
        setFrom(FROM);
        addRecipients(MembersSet.Staff);
        send(talk);
    }
    
    public static void contact(Email email) {
        setSubject(email.subject);
        setFrom(email.from != null ? email.from.email : FROM);
        addRecipients(email.recipients);
        send(email);
    }
    
    public static void mailing(Email email, Member recipient) {
        setSubject("Contact utilisateur Link-IT : " + email.subject);
        setFrom(email.from.email);
        addRecipient(recipient.email);
        send(email);
    }
    
    private static void addRecipients(MembersSet set) {
        final List<Member> members = MembersSetQueryFactory.create(set).find();
        for (Member member : members) {
            addRecipient(member.email);
        }
    }
}
