package models.activity;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import models.Member;
import models.ProviderType;
import models.Session;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;
import play.data.validation.Required;
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
            @Index(name="Activity_IDX", columnNames={Activity.PROVIDER, Activity.AT}),
            @Index(name="Activity_member_IDX", columnNames={Activity.MEMBER_FK, Activity.AT}),
            @Index(name="Activity_session_IDX", columnNames={Activity.SESSION_FK, Activity.AT})
        }
)
public abstract class Activity extends Model implements Comparable<Activity> {

    static final String SESSION_FK = "session_id";
    static final String MEMBER_FK = "member_id";
    static final String PROVIDER = "provider";
    static final String AT = "at";

    @Required
    @Column(name=PROVIDER)
    @Enumerated(EnumType.STRING)
    public ProviderType provider;

    @ManyToOne @JoinColumn(name=MEMBER_FK)
    public Member member;
    
    @ManyToOne @JoinColumn(name=SESSION_FK)
    public Session session;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name=AT)
    @Index(name=AT+"_idx")
    public Date at;
    
    protected Activity(ProviderType provider) {
        this.provider = provider;
        this.at = new Date();
    }
    
    protected Activity(ProviderType provider, Date at) {
        this.provider = provider;
        this.at = at;
    }
    
    public static List<Activity> recents(int page, int length) {
        return Activity.find("provider=? order by at desc", ProviderType.LinkIt).fetch(page, length);
    }
    
    /**
     * Activities for a given member
     * @param m member whose activities are to be found
     * @param page
     * @param length
     * @return 
     */
    public static List<Activity> recentsByMember(Member m, List<ProviderType> providers, int page, int length) {
        return Activity.find("from Activity a where a.member = ? and a.provider in (:providers) order by a.at desc", m)
        	.bind("providers", providers)
        	.fetch(page, length);
    }
    
    /**
     * Incoming activities of linked members of a given member 
     * @param m
     * @param page
     * @param length
     * @return 
     */
    public static List<Activity> recentsForMember(Member m, List<ProviderType> providers, int page, int length) {
        List<Activity> activities = Collections.emptyList();
        if (!m.links.isEmpty()) {
            activities = Activity.find("from Activity a where a.member in (:linked) and a.provider in (:providers) order by a.at desc")
                    .bind("linked", m.links)
                    .bind("providers", providers)
                    .fetch(page, length);
        }
        return activities;
    }
    
    public static List<Activity> recentsBySession(Session s, int page, int length) {
        return Activity.find("from Activity a where a.session = ? order by a.at desc", s).fetch(page, length);
    }
    
    final protected String getMessageKey() {
        return getClass().getSimpleName()+".message";
    }
    
    public abstract String getMessage(final String lang);
    public abstract String getUrl();

    public ProviderType getProvider() {
        return provider;
    }

    public int compareTo(Activity other) {
        return (other.at.compareTo(this.at));
    }

}