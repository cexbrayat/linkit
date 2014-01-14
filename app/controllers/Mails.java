package controllers;

import models.Member;
import models.Session;
import models.Setting;
import models.activity.Activity;
import models.activity.CommentActivity;
import models.mailing.Mailing;
import models.mailing.MembersSet;
import models.mailing.MembersSetQueryFactory;
import play.Play;
import play.mvc.Mailer;

import java.util.Collection;
import java.util.List;

/**
 * Controlleur d'envoi de mails
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class Mails extends Mailer {

    private static final String FROM_MIXIT = Play.configuration.getProperty("linkit.mail.from");

    public static void updateSession(Session talk) {
        setSubject("Notification Link-IT : session \"%s\" mise à jour", talk);
        setFrom(FROM_MIXIT);
        addRecipients(MembersSet.Staff);
        send(talk);
    }
    
    public static void contact(Mailing email) {
        setSubject("[Contact Mix-IT] - %s", email.subject);
        setFrom(email.from != null ? email.from.email : email.email);
        addRecipients(email.recipients);
        send(email);
    }
    
    public static void mailing(Mailing mailing, Member recipient) {
        setSubject("[Mix-IT] - %s", mailing.subject);
        setFrom(FROM_MIXIT);
        addRecipient(recipient.email);
        send(mailing);
        mailing.addActualRecipient(recipient);
    }
    
    public static void notification(Member recipient, Setting setting, Collection<? extends Activity> activities) {
        setSubject("[Mix-IT] - Que s'est-il passé?");
        setFrom(FROM_MIXIT);
        addRecipient(recipient.email);
        send(recipient, setting, activities);
    }
    
    private static void addRecipients(MembersSet set) {
        final List<? extends Member> members = MembersSetQueryFactory.create(set).find();
        for (Member member : members) {
            addRecipient(member.email);
        }
    }

    public static void commentNotification(Member recipient, CommentActivity activity) {
        setSubject("[Mix-IT] - Un commentaire pour vous!");
        setFrom(FROM_MIXIT);
        addRecipient(recipient.email);
        send(recipient, activity);
    }
}
