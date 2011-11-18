package models.activity;

import controllers.badge.BadgeComputationContext;
import java.util.Collection;
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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(appliesTo = "Activity",
indexes = {
    @Index(name = "Activity_IDX", columnNames = {Activity.PROVIDER, Activity.AT}),
    @Index(name = "Activity_member_provider_IDX", columnNames = {Activity.MEMBER_FK, Activity.PROVIDER, Activity.AT}),
    @Index(name = "Activity_session_IDX", columnNames = {Activity.SESSION_FK, Activity.AT})
})
public abstract class Activity extends Model implements Comparable<Activity> {

    static final String SESSION_FK = "session_id";
    static final String MEMBER_FK = "member_id";
    static final String PROVIDER = "provider";
    static final String AT = "at";

    @Required
    @Column(name = PROVIDER, nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    public ProviderType provider;

    /** Optional corresponding member. May be null. */
    @ManyToOne
    @JoinColumn(name = MEMBER_FK)
    public Member member;

    /** Optional corresponding session. May be null. */
    @ManyToOne
    @JoinColumn(name = SESSION_FK)
    public Session session;

    /** Timestamp of activity */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = AT)
    @Index(name = AT + "_idx")
    public Date at;
    
    /** True if badge computation has been done for this activity (or if it is pointless). */
    public boolean badgeComputationDone = false;

    protected Activity(ProviderType provider) {
        this(provider, new Date());
    }

    protected Activity(ProviderType provider, Date at) {
        this.provider = provider;
        this.at = at;
    }

    public static List<Activity> recents(int page, int length) {
        return Activity.find("provider=? order by at desc", ProviderType.LinkIt).fetch(page, length);
    }

    /**
     * Activities by a given member
     * @param m member whose activities are to be found
     * @param page
     * @param length
     * @return 
     */
    public static List<Activity> recentsByMember(Member m, Collection<ProviderType> providers, int page, int length) {
        CriteriaBuilder builder = em().getCriteriaBuilder();
        CriteriaQuery<Activity> cq = builder.createQuery(Activity.class);
        Root<Activity> activity = cq.from(Activity.class);
        Predicate givenMember = builder.equal(activity.get("member"), m);
        Predicate chosenProviders = builder.in(activity.get("provider")).value(providers);
        if (providers != null && !providers.isEmpty()) {
            cq.where(givenMember, chosenProviders);
        } else {
            cq.where(givenMember);
        }
        cq.orderBy(builder.desc(activity.get("at")));
        return em().createQuery(cq).setFirstResult((page-1) * length).setMaxResults(length).getResultList();

    }

    /**
     * Incoming activities of linked members of a given member 
     * @param m
     * @param page
     * @param length
     * @return 
     */
    public static List<Activity> recentsForMember(Member m, Collection<ProviderType> providers, int page, int length) {   
        List<Activity> activities = Collections.emptyList();
        if (!m.links.isEmpty()) {
            CriteriaBuilder builder = em().getCriteriaBuilder();
            CriteriaQuery<Activity> cq = builder.createQuery(Activity.class);
            Root<Activity> activity = cq.from(Activity.class);
            Predicate linkedMembers = builder.in(activity.get("member")).value(m.links);
            Predicate chosenProviders = builder.in(activity.get("provider")).value(providers);
            if (providers != null && !providers.isEmpty()) {
                cq.where(linkedMembers, chosenProviders);
            } else {
                cq.where(linkedMembers);
            }
            cq.orderBy(builder.desc(activity.get("at")));
            activities = em().createQuery(cq).setFirstResult((page-1) * length).setMaxResults(length).getResultList();
        }
        return activities;
    }

    public static List<Activity> recentsBySession(Session s, int page, int length) {
        return Activity.find("from Activity a where a.session = ? order by a.at desc", s).fetch(page, length);
    }

    final protected String getMessageKey() {
        return getClass().getSimpleName() + ".message";
    }

    /**
     * @param lang Language selected by user
     * @return i18n (HTML) message to be displayed on GUI for this activity
     */
    public abstract String getMessage(final String lang);

    /**
     * @return URL to be linked on this activity.
     */
    public abstract String getUrl();
    
    /**
     * @return Activities for which badge computation hasn't been done yet
     */
    public static List<Activity> uncomputed() {
        return Activity.find("badgeComputationDone=false").fetch();
    }

    public final void computeBadges(BadgeComputationContext context) {

        computedBadgesForConcernedMembers(context);

        // Flagging current activity as computed (whatever if we earned badges or not)
        this.badgeComputationDone = true;
        save();
    }

    protected abstract void computedBadgesForConcernedMembers(BadgeComputationContext context);
    
    public int compareTo(Activity other) {
        return (other.at.compareTo(this.at));
    }
}