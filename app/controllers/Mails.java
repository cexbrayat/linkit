package controllers;

import java.util.Collection;
import java.util.List;
import models.mailing.Mailing;
import models.Member;
import models.Session;
import models.Setting;
import models.activity.Activity;
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
        setSubject("Notification Link-IT : session \"%s\" mise à jour", talk);
        setFrom(FROM);
        addRecipients(MembersSet.Staff);
        send(talk);
    }
    
    public static void contact(Mailing email) {
        setSubject("[Contact Mix-IT] - %s", email.subject);
        setFrom(email.from != null ? email.from.email : FROM);
        addRecipients(email.recipients);
        send(email);
    }
    
    public static void mailing(Mailing mailing, Member recipient) {
        setSubject("[Mix-IT 2012] - %s", mailing.subject);
        setFrom(FROM);
        addRecipient(recipient.email);
        send(mailing);
        mailing.addActualRecipient(recipient);
    }
    
    public static void notification(Member recipient, Setting setting, Collection<Activity> activities) {
        setSubject("[Mix-IT 2012] - Que s'est-il passé?");
        setFrom(FROM);
        addRecipient(recipient.email);
        send(recipient, setting, activities);
    }
    
    private static void addRecipients(MembersSet set) {
        final List<Member> members = MembersSetQueryFactory.create(set).find();
        for (Member member : members) {
            addRecipient(member.email);
        }
    }
}
