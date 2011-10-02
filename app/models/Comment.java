package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(
        appliesTo="Comment"
//      indexes={@Index(name="Comment_IDX", columnNames={"session", "postedAt"})}
)
public class Comment extends Model {

    @Required
    @ManyToOne
    public Member author;
    
    @Required
    @ManyToOne
    public Session session;
    
    @Required
    @Temporal(TemporalType.TIMESTAMP)
    public Date postedAt;
    
    /** Optional comment this one replied to */
    @ManyToOne
    public Comment inReplyTo;
    
    /** Markdown enabled */
    @Lob
    @Required
    public String content;

    public Comment(Member author, Session session, String content) {
        this.author = author;
        this.session = session;
        this.content = content;
        this.postedAt = new Date();
    }

    public Comment(Member author, Session session, String content, Comment inReplyTo) {
        this(author, session, content);
        this.inReplyTo = inReplyTo;
    }

    @Override
    public String toString() {
        return author + " le " + postedAt;
    }
}
