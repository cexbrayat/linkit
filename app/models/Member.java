package models;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import controllers.JobFetchUserTimeline;
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
                        + "left outer join fetch m.lightningTalks "
                        + "left outer join fetch m.sharedLinks "
                        + "where m.login=:login")
})
public class Member extends Model implements Lookable {

    static final String QUERY_BYLOGIN = "MemberByLogin";
    static final String QUERY_FORPROFILE = "MemberForProfile";

    /**
     * Internal login : functional key
     */
    @Column(nullable = false, unique = true, updatable = true)
    @IndexColumn(name = "login_UK_IDX", nullable = false)
    @Required
    public String login;

    @Required
    public String email;

    @Required
    public String firstname;

    @Required
    public String lastname;

    @Required
    public String company;

    @Required
    @Temporal(TemporalType.TIMESTAMP)
    public Date registeredAt = new Date();

    /**
     * User-defined description, potentially as MarkDown
     */
    @Required
    @MaxSize(140)
    public String shortDescription;

    /**
     * User-defined description, potentially as MarkDown
     */
    @Lob
    @Required
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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    // FIXME CLA Refactor Member.accounts to Map<ProviderType,Account>?
    public Set<Account> accounts = new HashSet<Account>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    public Set<Interest> interests = new TreeSet<Interest>();

    @ElementCollection
    public Set<Badge> badges = EnumSet.noneOf(Badge.class);

    @OneToMany(mappedBy = "speaker", cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<LightningTalk> lightningTalks = new HashSet<LightningTalk>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "ordernum")
    @MaxSize(5)
    public List<SharedLink> sharedLinks = new ArrayList<SharedLink>();

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
            this.accounts.add(account);
        }
    }

    public final void removeAccount(Account account) {
        if (account != null) {
            this.accounts.remove(account);
            account.member = null;
            StatusActivity.deleteForMember(this, account.provider);
        }
    }

    /**
     * Find an activated social network account for given provider
     *
     * @param provider Provider searched
     * @return Activated account found, null otherwise
     */
    public Account getAccount(final ProviderType provider) {
        // FIXME CLA Refactor Member.accounts to Map<ProviderType,Account>?
        Predicate<Account> p = new Predicate<Account>() {
            public boolean apply(Account a) {
                return a.provider == provider;
            }
        };
        return Iterables.find(accounts, p, null);
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
        // FIXME CLA Refactor Member.accounts to Map<ProviderType,Account>?
        List<ProviderType> providers = Lists.newArrayList(Collections2.transform(this.accounts, new Function<Account, ProviderType>() {
            public ProviderType apply(Account a) {
                return a.provider;
            }
        }));
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
        List<Account> orderedAccounts = Lists.newArrayList(accounts);
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

    public void addLink(Member linked) {
        if (linked != null) {
            // Avoid activity duplication
            if (!links.contains(linked)) {
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

    public static List<Member> findMembersInterestedIn(String interest) {
        return Member.find(
                "select distinct m from Member m join m.interests as i where i.name = ?", interest).fetch();
    }

    public void addBadge(Badge badge) {
        // Avoid activity duplication
        if (!this.badges.contains(badge)) {
            this.badges.add(badge);
            new EarnBadgeActivity(this, badge).save();
        }
    }

    public void addSharedLink(SharedLink link) {
        link.member = this;
        this.sharedLinks.add(link);
        link.save();

        new SharedLinkActivity(link).save();
    }

    public void addLightningTalk(LightningTalk talk) {
        talk.speaker = this;
        this.lightningTalks.add(talk);
    }

    /**
     * Register a new SharedLink-IT user with given authentication account
     */
    public Member register(AuthAccount account) {
        save();
        authenticate(account);
        account.save();
        new SignUpActivity(this).save();
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
     */
    public Member updateProfile() {
        save();
        new UpdateProfileActivity(this).save();
        new JobFetchUserTimeline(this).now();
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
        return new StringBuilder()
                .append(firstname)
                .append(' ')
                .append(lastname)
                .append(" (")
                .append(login)
                .append(')')
                .toString();
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
        for (Member linked : this.links) {
            this.removeLink(linked);
        }
        for (Member linker : this.linkers) {
            linker.removeLink(this);
            linker.save();
        }
        return super.delete();
    }

    public static List<Member> recents(int page, int length) {
        return find("order by registeredAt desc").fetch(page, length);
    }
}