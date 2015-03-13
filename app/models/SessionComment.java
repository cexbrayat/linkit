package models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.google.common.collect.Sets;
import play.data.validation.Required;
import play.modules.search.Indexed;

import java.util.Collections;
import java.util.Set;

/**
 * A comment on a session talk.
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
@Indexed
public class SessionComment extends Comment {

    static final String SESSION_FK = "session_id";
    
    @Required
    @ManyToOne
    @JoinColumn(name=SESSION_FK)
    public Session session;

    public SessionComment(Member author, Session session, String content) {
        super(author, content);
        this.session = session;
    }
        
    /**
     * Delete all comments related to given session
     * @return 
     */
    public static int deleteForSession(Session session) {
        return delete("delete SessionComment sc where sc.session = ?", session);
    }

    @Override
    public Set<Member> getNotifiableMembers() {
        // Don't notify author of comment
        return Sets.difference(this.session.speakers, Collections.singleton(this.author));
    }
}
