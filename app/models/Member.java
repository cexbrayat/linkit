package models;

import controllers.JobFetchUserTimeline;
import helpers.badge.BadgeComputationContext;
import helpers.badge.BadgeComputer;
import helpers.badge.BadgeComputerFactory;
import java.util.*;
import javax.persistence.*;

import models.activity.EarnBadgeActivity;
import models.activity.LinkActivity;
import models.activity.LookProfileActivity;
import models.activity.SignUpActivity;
import models.activity.UpdateProfileActivity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.IndexColumn;
import play.Logger;
import play.data.validation.Required;
import play.db.jpa.*;

/**
 * A LinkIT member.
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
    + "where m.login=:login")
})
public class Member extends Model implements Lookable {

    static final String QUERY_BYLOGIN = "MemberByLogin";
    static final String QUERY_FORPROFILE = "MemberForProfile";
    
    /** Internal login : functional key */
    @Column(nullable = false, unique = true, updatable = false)
    @IndexColumn(name = "login_UK_IDX", nullable = false)
    @Required
    public String login;
    
    @Required
    public String email;
    public String firstname;
    public String lastname;
    
    /** Name under which he wants to be displayed */
    @Required
    public String displayName;
    
    /** User-defined description, potentially as MarkDown */
    @Lob
    @Required
    public String description;
    /** Twitter account name */
    public String twitterName;
    /** Google+ ID, i.e https://plus.google.com/{ThisFuckingLongNumberInsteadOfABetterId} as seen on Google+' profile link */
    public String googlePlusId;
    /**
     * Members he follows
     */
    @ManyToMany()
    public Set<Member> links = new HashSet<Member>();
    /**
     * Members who follow him : reverse-mapping of {@link Member#links}
     */
    @ManyToMany(mappedBy = "links")
    public Set<Member> linkers = new HashSet<Member>();
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<Account> accounts = new HashSet<Account>();
    @ManyToMany(cascade = CascadeType.PERSIST)
    public Set<Interest> interests = new TreeSet<Interest>();
    @ElementCollection
    public Set<Badge> badges = EnumSet.noneOf(Badge.class);
    
    /** Number of profile consultations */
    public long nbConsults;

    public Member(String login, Account account) {
        this.login = login;
        addAccount(account);
    }

    public final void addAccount(Account account) {
        if (account != null) {
            account.member = this;
            this.accounts.add(account);
            // On préinitialise son profil avec les données récupérées du compte
            account.initMemberProfile();
        }
    }

    /**
     * Find unique member having given login.
     * Seems this request is very often used, it's better to used it (more efficient with named query usage) instead of Play! find("byLogin", login)
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
        return CollectionUtils.exists(links, new Predicate() {

            public boolean evaluate(Object o) {
                Member linked = (Member) o;
                return loginToLink.equals(linked.login);
            }
        });
    }

    public boolean hasForLinker(final String loginToLink) {
        return CollectionUtils.exists(linkers, new Predicate() {

            public boolean evaluate(Object o) {
                Member linked = (Member) o;
                return loginToLink.equals(linked.login);
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

    /**
     * Register user a new Link-IT user
     */
    public Member register() {
        save();
        new SignUpActivity(this).save();
        return this;
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
        return new EqualsBuilder().append(this.login, other.login).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.login).toHashCode();
    }

    /**
     * Display member. WARNING : used on UI as main display of user.
     * @return 
     */
    @Override
    public String toString() {
        return displayName;
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
}