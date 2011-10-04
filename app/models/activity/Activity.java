package models.activity;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import models.Member;
import models.Session;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;
import play.db.jpa.Model;

/**
 * An activity on Link-IT site, i.e a persisted event
 * @author Agnes <agnes.crepet@gmail.com>
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Table(
        appliesTo="Activity",
        indexes={
            @Index(name="Activity_member_IDX", columnNames={Activity.MEMBER_FK, Activity.AT}),
            @Index(name="Activity_session_IDX", columnNames={Activity.SESSION_FK, Activity.AT})
        }
)
public abstract class Activity extends Model {

    static final String SESSION_FK = "session_id";
    static final String MEMBER_FK = "member_id";
    static final String AT = "at";

    @ManyToOne @JoinColumn(name=MEMBER_FK)
    public Member member;
    
    @ManyToOne @JoinColumn(name=SESSION_FK)
    public Session session;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name=AT)
    @Index(name=AT+"_idx")
    public Date at;
    
    protected Activity() {
        this.at = new Date();
    }
    
    public static List<Activity> recents(int page, int length) {
        return Activity.find("order by at desc").fetch(page, length);
    }
    
    public static List<Activity> recentsByMember(Member m, int max) {
        return Activity.find("from Activity a where a.member = ? order by at desc", m).fetch(max);
    }
    
    public static List<Activity> recentsBySession(Session s, int max) {
        return Activity.find("from Activity a where a.session = ? order by at desc", s).fetch(max);
    }
    
    final protected String getMessageKey() {
        return getClass().getSimpleName()+".message";
    }
    
    public abstract String getMessage(final String lang);
    public abstract String getUrl();
}