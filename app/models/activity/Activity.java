package models.activity;

import helpers.badge.BadgeComputationContext;
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
import models.Article;
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
    @Index(name = "Activity_article_IDX", columnNames = {Activity.ARTICLE_FK, Activity.AT}),
    @Index(name = "Activity_session_IDX", columnNames = {Activity.SESSION_FK, Activity.AT})
})
public abstract class Activity extends Model implements Comparable<Activity> {

    static final String ARTICLE_FK = "article_id";
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

    /** Optional corresponding article. May be null. */
    @ManyToOne
    @JoinColumn(name = ARTICLE_FK)
    public Article article;

    /** Timestamp of activity */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = AT)
    @Index(name = AT + "_idx")
    public Date at;
    
    /** True if activity is important, and should be displayed in general feed */
    public boolean important = true;
    
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
        return Activity.find("provider=? and important=true order by at desc", ProviderType.LinkIt).fetch(page, length);
    }

    /**
     * Recent dates of Link-IT activity by given member in desc order
     * @param member
     * @param page
     * @param length
     * @return 
     */
    public static List<Date> recentDatesByMember(final Member member, int page, int length) {
        return Activity.find("select a.at from Activity a where a.provider=? and a.member=? order by at desc", ProviderType.LinkIt, member).fetch(page, length);
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

    public static List<Activity> recentsByArticle(Article a, int page, int length) {
        return Activity.find("from Activity a where a.article = ? order by a.at desc", a).fetch(page, length);
    }
    
    /**
     * Delete all activities related to given member
     * @param member
     * @return 
     */
    public static int deleteForMember(Member member) {
        return delete("delete Activity a where a.member = ?1 or a.other = ?1", member);
    }
    
    /**
     * Delete all activities related to given member for given provider
     * @param member
     * @param provider
     * @return 
     */
    public static int deleteForMember(Member member, ProviderType provider) {
        return delete("delete Activity a where (a.member = ?1 or a.other = ?1) and a.provider = ?2", member, provider);
    }
    
    /**
     * Delete all activities related to given member for given provider
     * @return 
     */
    public static int deleteForArticle(Article article) {
        return delete("delete Activity a where a.article = ?", article);
    }
    
    /**
     * Delete all activities related to given session
     * @return 
     */
    public static int deleteForSession(Session session) {
        return delete("delete Activity a where a.session = ?", session);
    }

    final protected String getMessageKey() {
        return getClass().getSimpleName() + ".message";
    }

    /**
     * @return URL to be linked on this activity.
     */
    public abstract String getUrl();
    
    /**
     * @return IDs of Activities for which badge computation hasn't been done yet
     */
    public static List<Long> uncomputedIds() {
        return Activity.find("select a.id from Activity a where a.badgeComputationDone=false").fetch();
    }

    // CLA 14/12/2011 : inherits Play! findById generates "org.hibernate.PropertyAccessException", which seems related to far more eager fetching.
    public static <T extends Activity> T findById(Long id) {
        return find("id=?", id).first();
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