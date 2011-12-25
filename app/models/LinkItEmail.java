package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import play.Logger;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.db.jpa.Model;
import play.libs.Mail;
import play.modules.search.Field;

/**
 * A generic Email entity
 * @author Agnes <agnes.crepet@gmail.com>
 */
@Entity
public class LinkItEmail extends Model {

    static final String SENTAT = "sentAt";
    @Required
    @ManyToOne
    public Member from;
    @Required
    @Valid
    @Field
    public String subject;
    /** Markdown enabled */
    @Lob
    @Required
    @Valid
    @Field
    public String message;
    @Column(name = SENTAT)
    @Required
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date sentAt;
    @ManyToMany
    @Required
    public List<Member> recipients = new ArrayList<Member>();

    public LinkItEmail(Member from, String subject, String message, List<Member> recipients) {
        this.from = from;
        this.message = message;
        this.subject = subject;
        this.recipients = recipients;
        this.sentAt = new Date();

    }

    @Override
    public String toString() {
        return from + " le " + sentAt;
    }

    public static List<LinkItEmail> recents(int page, int length) {
        return find("order by sentAt desc").fetch(page, length);
    }

    public void send() {
        try {
            SimpleEmail email = new SimpleEmail();
            email.setFrom(from.email);
            Iterator it = recipients.iterator();
            while (it.hasNext()) {
                Member to = (Member) it.next();
                email.addTo(to.email);
            }
            email.setSubject(subject);
            email.setMsg(message);
            Mail.send(email);
            this.save();
        } catch (EmailException ex) {
            Logger.error(ex.getMessage());
        }
    }
}
