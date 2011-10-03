package models.activity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import models.Comment;
import models.Member;
import models.Session;
import play.data.validation.Required;
import play.i18n.Messages;

/**
 * A comment activity : someone ({@link Activity#member} commented on a session ({@link Activity#session}
 * @author Agnes <agnes.crepet@gmail.com>
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class CommentActivity extends Activity {

    @Required
    @ManyToOne
    public Comment comment;

    public CommentActivity(Member author, Session session, Comment comment) {
        super();
        this.member = author;
        this.session = session;
        this.comment = comment;
    }

    @Override
    public String getMessage(String lang) {
        return Messages.get(getMessageKey(), member, session, comment);
    }
}
