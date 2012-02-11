package models.activity;

import helpers.badge.BadgeComputationContext;
import models.Article;
import models.Member;
import models.ProviderType;
import models.Session;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

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
@NamedQueries({
       // Membres ordonnés par date de dernière activité (qu'il y en ait ou non)
        @NamedQuery(name = Activity.QUERY_ORDEREDMEMBERS,
                query = "select distinct(m), max(a.at) "
                        + "from Activity a "
                        + "right outer join a.member m "
                        + "group by m "
                        + "order by max(a.at) desc")
})
public abstract class Activity extends Model implements Comparable<Activity> {

    static final String ARTICLE_FK = "article_id";
    static final String SESSION_FK = "session_id";
    static final String MEMBER_FK = "member_id";
    static final String PROVIDER = "provider";
    static final String AT = "at";
    static final String IMPORTANT = "important";
    static final String QUERY_ORDEREDMEMBERS = "ActivityOrderedMembers";

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
    @Column(name = IMPORTANT)
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

    public static List<Activity> notifiablesBetween(Date start, Date end) {
        CriteriaBuilder builder = em().getCriteriaBuilder();
        CriteriaQuery<Activity> cq = builder.createQuery(Activity.class);
        Root<Activity> activity = cq.from(Activity.class);
        Predicate provider = builder.equal(activity.get(PROVIDER), ProviderType.LinkIt);
        Predicate important = builder.equal(activity.get(IMPORTANT), Boolean.TRUE);
        Predicate where = builder.and(provider, important);
        if (start != null) {
            Predicate after = builder.greaterThanOrEqualTo(activity.get(AT).as(Date.class), start);
            where = builder.and(where, after);
        }
        if (end != null) {
            Predicate before = builder.lessThan(activity.get(AT).as(Date.class), end);
            where = builder.and(where, before);
        }
        cq.where(where);
        cq.orderBy(builder.desc(activity.get(AT)));
        return em().createQuery(cq).getResultList();
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
        Predicate chosenProviders = builder.in(activity.get(PROVIDER)).value(providers);
        Predicate important = builder.equal(activity.get(IMPORTANT), Boolean.TRUE);
        if (providers != null && !providers.isEmpty()) {
            cq.where(givenMember, chosenProviders, important);
        } else {
            cq.where(givenMember, important);
        }
        cq.orderBy(builder.desc(activity.get(AT)));
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
            Predicate chosenProviders = builder.in(activity.get(PROVIDER)).value(providers);
            Predicate important = builder.equal(activity.get(IMPORTANT), Boolean.TRUE);
            if (providers != null && !providers.isEmpty()) {
                cq.where(linkedMembers, chosenProviders, important);
            } else {
                cq.where(linkedMembers, important);
            }
            cq.orderBy(builder.desc(activity.get(AT)));
            activities = em().createQuery(cq).setFirstResult((page-1) * length).setMaxResults(length).getResultList();
        }
        return activities;
    }

    /**
     * Incoming activities for a given member between 2 dates
     * @param m
     * @param start may be null
     * @param end may be null
     * @return 
     */
    public static List<Activity> notifiablesForBetween(Member m, Date start, Date end) {   
        List<Activity> activities = Collections.emptyList();
        if (!m.links.isEmpty()) {
            CriteriaBuilder builder = em().getCriteriaBuilder();
            CriteriaQuery<Activity> cq = builder.createQuery(Activity.class);
            Root<Activity> activity = cq.from(Activity.class);
            Predicate where = builder.in(activity.get("member")).value(m.links);
            if (start != null) {
                Predicate after = builder.greaterThanOrEqualTo(activity.get(AT).as(Date.class), start);
                where = builder.and(where, after);
            }
            if (end != null) {
                Predicate before = builder.lessThan(activity.get(AT).as(Date.class), end);
                where = builder.and(where, before);
            }
            where = builder.and(where, builder.equal(activity.get(IMPORTANT), Boolean.TRUE));
            cq.where(where);
            cq.orderBy(builder.desc(activity.get(AT)));
            activities = em().createQuery(cq).getResultList();
        }
        return activities;
    }

    public static List<Activity> recentsBySession(Session s, int page, int length) {
        return Activity.find("from Activity a where a.session = ? order by a.at desc", s).fetch(page, length);
    }

    public static List<Activity> recentsByArticle(Article a, int page, int length) {
        return Activity.find("from Activity a where a.article = ? order by a.at desc", a).fetch(page, length);
    }
    
    public static class OrderedMembersDTO {
        private List<Member> members = new ArrayList<Member>();
        /** Key : Member, Value : Date of latest activity, may be null */
        private Map<Member, Date> latestActivityDateByMember = new HashMap<Member, Date>();

        protected void add(Member member, Date latestActivity) {
            members.add(member);
            latestActivityDateByMember.put(member, latestActivity);
        }
        
        public Date getLatestActivityFor(Member member) {
            return latestActivityDateByMember.get(member);
        }

        public List<Member> getMembers() {
            return members;
        }
    }
    
    public static OrderedMembersDTO findOrderedMembers() {
        OrderedMembersDTO members = new OrderedMembersDTO();

        List<Object[]> resultset = (List) em().createNamedQuery(QUERY_ORDEREDMEMBERS).getResultList();
        for (Object[] result : resultset) {
            members.add((Member) result[0], (Date) result[1]);
        }
        return members;
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

        // Flagging current activity as computed (whenever we earned badges or not)
        this.badgeComputationDone = true;
        save();
    }

    protected abstract void computedBadgesForConcernedMembers(BadgeComputationContext context);
    
    public int compareTo(Activity other) {
        return (other.at.compareTo(this.at));
    }

    /*public Activity save(){
        super.save();
        LiveActivities.liveStream.publish(this);
        return this;
    }*/
}