package models;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import controllers.JobFetchUserTimeline;
import controllers.JobMajUserRegisteredTicketing;
import helpers.badge.BadgeComputationContext;
import helpers.badge.BadgeComputer;
import helpers.badge.BadgeComputerFactory;
import models.activity.*;
import models.auth.AuthAccount;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.IndexColumn;
import play.Logger;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.*;
import java.util.List;
import models.mailing.Mailing;
import org.apache.commons.lang.WordUtils;
import play.Play;
import play.data.validation.Email;
import play.data.validation.Valid;
import play.modules.search.Field;
import play.modules.search.Indexed;

/**
 * A LinkIT member.
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries({
        @NamedQuery(name = Member.QUERY_BYLOGIN, query = "from Member m where m.login=:login"),
        @NamedQuery(name = Member.QUERY_FORPROFILE,
                query = "select m from Member m "
                        + "left outer join fetch m.links "
                        + "left outer join fetch m.linkers "
                        + "left outer join fetch m.badges "
                        + "left outer join fetch m.interests "
                        + "left outer join fetch m.sessions "
                        + "left outer join fetch m.sharedLinks "
                        + "where m.login=:login")
})
@Indexed
public class Member extends Model implements Lookable, Comparable<Member> {

    public static final String FIRSTNAME = "firstname";
    public static final String LASTNAME = "lastname";
    public static final String COMPANY = "company";
    public static final String SHORTDESCRIPTION = "shortDescription";
    public static final String LONGDESCRIPTION = "longDescription";

    static final String QUERY_BYLOGIN = "MemberByLogin";
    static final String QUERY_FORPROFILE = "MemberForProfile";

    static final String CACHE_ACCOUNT_PREFIX = "account_";
    
    static final char[] CHAR_DELIMITER_NAME = {'-',' ','_','.'};
    
    static final int JOBS_DELAY_AFTER_UPDATE = Integer.valueOf(Play.configuration.getProperty("linkit.job.delayAfterMemberUpdate", "2"));
    
    /**
     * Internal login : functional key
     */
    @Column(nullable = false, unique = true, updatable = true)
    @IndexColumn(name = "login_UK_IDX", nullable = false)
    @Required
    public String login;

    @Required
    @Email
    public String email;

    @Column(name = FIRSTNAME)
    @Required
    @Field
    public String firstname;

    @Column(name = LASTNAME)
    @Required
    @Field
    public String lastname;

    @Column(name = COMPANY)
    @Field
    public String company;
    
    public boolean ticketingRegistered = false;

    @Temporal(TemporalType.TIMESTAMP)
    public Date registeredAt = new Date();

    /**
     * User-defined description, potentially as MarkDown
     */
    @Column(name = SHORTDESCRIPTION)
    @Required
    @MaxSize(140)
    @Field
    public String shortDescription;

    /**
     * User-defined description, potentially as MarkDown
     */
    @Column(name = LONGDESCRIPTION)
    @Lob
    @Field
    public String longDescription;

    /**
     * Members he follows
     */
    @ManyToMany
    public Set<Member> links = new HashSet<Member>();
    /**
     * Members who follow him : reverse-mapping of {@link Member#links}
     */
    @ManyToMany(mappedBy = "links")
    public Set<Member> linkers = new HashSet<Member>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @MapKey(name="provider")
    public Map<ProviderType,Account> accounts = new EnumMap<ProviderType, Account>(ProviderType.class);

    @ManyToMany(cascade = CascadeType.PERSIST)
    public Set<Interest> interests = new TreeSet<Interest>();

    @ElementCollection
    public Set<Badge> badges = EnumSet.noneOf(Badge.class);

    @ManyToMany(mappedBy="speakers")
    public Set<Session> sessions = new HashSet<Session>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @OrderColumn(name = "ordernum")
    @Valid
    public List<SharedLink> sharedLinks = new LinkedList<SharedLink>();
    
    /**
     * Number of profile consultations
     */
    public long nbConsults;

    public Member(String login) {
        this.login = login;
    }

    public final void addAccount(Account account) {
        if (account != null) {
            account.member = this;
            this.accounts.put(account.provider, account);
        }
    }

    public final void removeAccount(Account account) {
        if (account != null) {
            this.accounts.remove(account.provider);
            account.member = null;
            // No need to delete related entities if Member not yet persisted (cf. https://trello.com/board/mix-it-2012/4f1b9ce056cf07e52f0072f7)
            if (this.id != null) {
                StatusActivity.deleteForMember(this, account.provider);
            }
        }
    }

    /**
     * Find an activated social network account for given provider
     *
     * @param provider Provider searched
     * @return Activated account found, null otherwise
     */
    public Account getAccount(final ProviderType provider) {
        return accounts.get(provider);
    }

    public GoogleAccount getGoogleAccount() {
        return (GoogleAccount) getAccount(ProviderType.Google);
    }

    public TwitterAccount getTwitterAccount() {
        return (TwitterAccount) getAccount(ProviderType.Twitter);
    }

    /**
     * Preserve {@link ProviderType} enumeration order (used on UI)
     *
     * @return All providers where the member has an activated social network account
     */
    public List<ProviderType> getAccountProviders() {
        List<ProviderType> providers = Lists.newArrayList(accounts.keySet());
        providers.add(ProviderType.LinkIt);   // LinkIt est toujours un provider actif
        Collections.sort(providers);    // Ensure enumeration order
        return providers;
    }

    /**
     * Preserve {@link ProviderType} enumeration order (used on UI)
     *
     * @return All social network accounts
     */
    public List<Account> getOrderedAccounts() {
        List<Account> orderedAccounts = Lists.newArrayList(accounts.values());
        Collections.sort(orderedAccounts);
        return orderedAccounts;
    }

    /**
     * Find unique member having given login.
     * Seems this request is very often used, it's better to used it (more efficient with named query usage) instead of Play! find("byLogin", login)
     *
     * @param login Login to find. May be null.
     * @return Member found, null if none (or if login null).
     */
    public static <M extends Member> M findByLogin(final String login) {
        M member = null;
        if (login != null) {
            try {
                member = (M) em().createNamedQuery(QUERY_BYLOGIN).setParameter("login", login).getSingleResult();
            } catch (NoResultException ex) {
                member = null;
            }
        }
        return member;
    }

    /**
     * Fetch a Member with all associated data for Profile display
     */
    public static <M extends Member> M fetchForProfile(final String login) {
        M member = null;
        try {
            member = (M) em().createNamedQuery(QUERY_FORPROFILE).setParameter("login", login).getSingleResult();
        } catch (NoResultException ex) {
            member = null;
        }
        return member;
    }

    public static Member findByEmail(final String email) {
        return find("email=?", email).first();
    }
        
    public static List<Long> findAllIds() {
        return find("select m.id from Member m").fetch();
    }

    public void addLink(Member linked) {
        if (linked != null) {
            // Avoid activity duplication and auto-linking
            if (!links.contains(linked) && !equals(linked)) {
                links.add(linked);
                linked.linkers.add(this);

                new LinkActivity(this, linked).save();
            }
        }
    }

    public void removeLink(Member memberToUnlink) {
        if (memberToUnlink != null) {
            links.remove(memberToUnlink);
            memberToUnlink.linkers.remove(this);
        }
    }

    public static void addLink(String login, String loginToLink) {
        Member member = Member.findByLogin(login);
        Member memberToLink = Member.findByLogin(loginToLink);
        member.addLink(memberToLink);
        member.save();
    }

    public static void removeLink(String login, String loginToUnlink) {
        Member member = Member.findByLogin(login);
        Member memberToUnlink = Member.findByLogin(loginToUnlink);
        member.removeLink(memberToUnlink);
        member.save();
    }

    public boolean isLinkedTo(final String loginToLink) {
        return Iterables.any(links, new Predicate<Member>() {
            public boolean apply(Member linked) {
                return loginToLink.equals(linked.login);
            }
        });
    }

    public boolean hasForLinker(final String loginToLink) {
        return Iterables.any(linkers, new Predicate<Member>() {
            public boolean apply(Member linker) {
                return loginToLink.equals(linker.login);
            }
        });
    }

    public Member addInterest(String interest) {
        if (StringUtils.isNotBlank(interest)) {
            interests.add(Interest.findOrCreateByName(interest));
        }
        return this;
    }

    public Member addInterests(String... interests) {
        for (String interet : interests) {
            addInterest(interet);
        }
        return this;
    }

    public Member updateInterests(String... interests) {
        this.interests.clear();
        addInterests(interests);
        return this;
    }

    public static List<Member> findMembersInterestedIn(Interest interest) {
        return Member.find(
                "select distinct m from Member m join m.interests as i where i = ?", interest).fetch();
    }

    public void addBadge(Badge badge) {
        // Avoid activity duplication
        if (!this.badges.contains(badge)) {
            this.badges.add(badge);
            new EarnBadgeActivity(this, badge).save();
        }
    }

    public void addSharedLink(SharedLink link) {
        addSharedLink(link, true);
    }

    private void addSharedLink(SharedLink link, boolean newActivity) {
        if (!this.sharedLinks.contains(link)) {
            link.member = this;
            link.ordernum = this.sharedLinks.size();
            this.sharedLinks.add(link);

            if (newActivity && this.id != null) {
                link.save();
                new SharedLinkActivity(link).save();
            }
        }
    }

    public void removeSharedLink(SharedLink link) {
        int i = 0;
        for (Iterator<SharedLink> iLink = this.sharedLinks.iterator(); iLink.hasNext(); ) {
            SharedLink current = iLink.next();
            if (current.equals(link)) {
                current.delete();
                iLink.remove();
            } else {
                current.ordernum = i++;
            }
        }
    }
    
    public void updateSharedLinks(List<SharedLink> links) {
        List<SharedLink> previouses = new ArrayList<SharedLink>(this.sharedLinks);
        this.sharedLinks.clear();
        for (final SharedLink link : links) {
            final Predicate<SharedLink> equals = new Predicate<SharedLink>() {
                public boolean apply(SharedLink other) {
                    return link.equals(other);
                }
            };
            SharedLink previous = Iterables.find(previouses, equals, null);
            if (previous == null) {
                addSharedLink(link, true);
            } else {
                addSharedLink(previous, false);
            }
        }
    }

    /**
     * Pre-register a new Link-IT user with given authentication account.
     * This member is not yet persisted. It will be when he fills in his profile, and when {@link #register(models.auth.AuthAccount)} is called.
     */
    public void preregister(AuthAccount account) {
        play.cache.Cache.add(login, this);
        play.cache.Cache.add(CACHE_ACCOUNT_PREFIX+this.login, account);        
        authenticate(account);
    }

    /**
     * @param login
     * @return Pre-registered Member with given login if exists.
     */
    public static Member getPreregistered(String login) {
        return (Member) play.cache.Cache.get(login);
    }

    /**
     * Register pre-registered member
     */
    public Member register() {
        return register(login);
    }

    /**
     * Register pre-registered member of given login (before hypothetical update of login)
     */
    public Member register(final String originalLogin) {
        AuthAccount account = (AuthAccount) play.cache.Cache.get(CACHE_ACCOUNT_PREFIX+originalLogin);
        save();
        account.save();
        new SignUpActivity(this).save();
        new JobFetchUserTimeline(this).in(JOBS_DELAY_AFTER_UPDATE);
        new JobMajUserRegisteredTicketing(this.id).in(JOBS_DELAY_AFTER_UPDATE);
        return this;
    }

    /**
     * User authenticated with given account
     *
     * @param account
     */
    public void authenticate(AuthAccount account) {
        if (account != null) {
            account.member = this;
            // On préinitialise son profil avec les données récupérées du compte
            account.initMemberProfile();
        }
    }

    /**
     * Update user profile
     * @param activity true if activities must be produced
     */
    public Member updateProfile(boolean activity) {
        save();
        if (activity) {
            new UpdateProfileActivity(this).save();
        }
        new JobFetchUserTimeline(this).in(JOBS_DELAY_AFTER_UPDATE);
        new JobMajUserRegisteredTicketing(this.id).in(JOBS_DELAY_AFTER_UPDATE);
        return this;
    }

    public void computeBadges(Set<Badge> potentialBadges, BadgeComputationContext context) {

        // Avoid to recompute an already earned badge
        potentialBadges.removeAll(badges);

        if (!potentialBadges.isEmpty()) {
            // Retrieving badge computers for thoses potential badges
            Set<BadgeComputer> computers = BadgeComputerFactory.getFor(potentialBadges);

            // Computing all granted badges
            Set<Badge> grantedBadges = EnumSet.noneOf(Badge.class);
            for (BadgeComputer computer : computers) {
                grantedBadges.addAll(computer.compute(this, context));
            }

            // Granting earned badges to member
            if (!grantedBadges.isEmpty()) {
                for (Badge badge : grantedBadges) {
                    Logger.debug("Le membre %s se voit attribuer le badge %s", this, badge);
                    addBadge(badge);
                }
                save();
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Member other = (Member) obj;
        return new EqualsBuilder()
                .append(this.id, other.id)
                .append(this.login, other.login)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.id)
                .append(this.login)
                .toHashCode();
    }

    /**
     * Display member. WARNING : used on UI as main display of user.
     *
     * @return
     */
    @Override
    public String toString() {
        return WordUtils.capitalizeFully(new StringBuilder()
                .append(firstname)
                .append(' ')
                .append(lastname)
                .toString()
                ,CHAR_DELIMITER_NAME);
        
    }

    public boolean hasRole(String profile) {
        return false;
    }

    public long getNbLooks() {
        return nbConsults;
    }

    public void lookedBy(Member member) {
        if (!this.equals(member)) {
            nbConsults++;
            save();
            if (member != null) {
                new LookProfileActivity(member, this).save();
            }
        }
    }

    @Override
    public Member delete() {
        Activity.deleteForMember(this);
        AuthAccount.deleteForMember(this);
        Comment.deleteForMember(this);
        Vote.deleteForMember(this);
        Set<Member> copyLinks = new HashSet(this.links);    // Avoid ConcurrentModificationException
        Mailing.deleteForMember(this);
        for (Member linked : copyLinks) {
            this.removeLink(linked);
        }
        Set<Member> copyLinkers = new HashSet(this.linkers);    // Avoid ConcurrentModificationException
        for (Member linker : copyLinkers) {
            linker.removeLink(this);
            linker.save();
        }
        for (Session session : this.sessions) {
            session.removeSpeaker(this);
            session.save();
            // FIXME What should happen is no more speakers in session?
            // For now, better to have a "no-speakers" session than to delete important ones.
        }
        return super.delete();
    }

    public static List<Member> recents(int page, int length) {
        return find("order by registeredAt desc").fetch(page, length);
    }

    public int compareTo(Member t) {
        return toString().compareTo(t.toString());
    }

    static class TalkPredicate implements Predicate<Session> {

        private boolean validated;

        public TalkPredicate(boolean validated) {
            this.validated = validated;
        }
        
        public boolean apply(Session s) {
            if (s instanceof Talk) {
                Talk t = (Talk) s;
                return t.valid == this.validated;
            } else {
                return false;
            }
        }
        
    }
    
    private static final Predicate<Session> VALIDATED_TALK = new TalkPredicate(true);
    private static final Predicate<Session> PROPOSED_TALK = new TalkPredicate(false);
    private static final Predicate<Session> LIGHTNING_TALK = new Predicate<Session>() {
        public boolean apply(Session s) {
            return (s instanceof LightningTalk);
        }
    };
    
    public boolean isSpeaker() {
        return Iterables.any(sessions, VALIDATED_TALK);
    }
    
    public boolean isLightningTalkSpeaker() {
        return Iterables.any(sessions, LIGHTNING_TALK);
    }
    
    public Set<Session> getValidatedTalks() {
        return Sets.filter(sessions, VALIDATED_TALK);
    }
    
    public Set<Session> getProposedTalks() {
        return Sets.filter(sessions, PROPOSED_TALK);
    }
    
    public Set<Session> getLightningTalks() {
        return Sets.filter(sessions, LIGHTNING_TALK);
    }

    public void setTicketingRegistered(boolean ticketingRegistered) {
        if (!this.ticketingRegistered && ticketingRegistered) {
            new BuyTicketActivity(this).save();
        }
        this.ticketingRegistered = ticketingRegistered;
    }
}