package models.mailing;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import models.Member;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.db.jpa.Model;

/**
 * A generic email entity
 * @author Agnes <agnes.crepet@gmail.com>
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class Mailing extends Model {

    @ManyToOne
    public Member from;

    @Required
    @Valid
    public String subject;

    /** Markdown enabled */
    @Lob
    @Required
    @Valid
    public String message;

    @Required
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date sentAt;

    @Enumerated(EnumType.STRING)
    @Required
    public MembersSet recipients;

    @ManyToMany
    public Set<Member> actualRecipients = new HashSet<Member>();

    @Enumerated(EnumType.STRING)
    @Required
    public MailingStatus status;
    
    public Mailing() {
        this.status = MailingStatus.Planned;
        this.sentAt = new Date();
    }

    public boolean isUpdatable() {
        return status != MailingStatus.Sent && actualRecipients.isEmpty();
    }
    
    public void send() {
        this.status = MailingStatus.Sending;
        save();
    }
    
    public void cancel() {
        this.status = MailingStatus.Planned;
        save();
    }
    
    @Override
    public String toString() {
        return subject;
    }

    public void addActualRecipient(Member recipient) {
        this.actualRecipients.add(recipient);
        this.save();
    }
    
    public Set<Member> getPendingRecipients() {
        // All recipients
        Set<Member> pendings = new HashSet<Member>(MembersSetQueryFactory.create(recipients).find());
        // Minus the ones already contacted
        pendings.removeAll(this.actualRecipients);
        return pendings;
    }
    
    public static List<Mailing> recents(int page, int length) {
        return find("order by sentAt desc").fetch(page, length);
    }

    public static List<Mailing> pending() {
        return find("status <> ? order by sentAt asc", MailingStatus.Sent).fetch();
    }
}
