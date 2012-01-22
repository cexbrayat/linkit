package models;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import models.mailing.MailingStatus;
import models.mailing.MembersSet;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.db.jpa.Model;

/**
 * A generic email entity
 * @author Agnes <agnes.crepet@gmail.com>
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class Email extends Model {

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
    
    public Email() {
        this.status = MailingStatus.Planned;
        this.sentAt = new Date();
    }

    public boolean isModifiable() {
        return status == MailingStatus.Planned;
    }
    
    public void send() {
        this.status = MailingStatus.Sending;
        // FIXME CLA Start job for batch sending mailings?
        save();
    }
    
    @Override
    public String toString() {
        return subject;
    }

    public static List<Email> recents(int page, int length) {
        return find("order by sentAt desc").fetch(page, length);
    }
}
