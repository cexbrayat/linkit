package models;

import java.util.*;
import javax.persistence.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.IndexColumn;
import play.data.validation.Required;
import play.db.jpa.*;

/**
 * A LinkIT member.
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@NamedQueries({
    @NamedQuery(name="MemberByLogin", query="from Member m where m.login=:login")
})
public class Member extends Model {

    /** Internal login : functional key */
    @Column(nullable = false, unique = true, updatable = false)
    @IndexColumn(name = "login_UK_IDX", nullable = false)
    @Required
    public String login;
    
    public String email;
    public String firstname;
    public String lastname;
    
    /** Name under which he wants to be displayed */
    public String displayName;

    /** User-defined description, potentially as MarkDown */
    public String description;
    
    /** Twitter account name */
    public String twitterName;
    
    /** Google+ ID, i.e https://plus.google.com/{ThisFuckingLongNumberInsteadOfABetterId} as seen on Google+' profile link */
    public String googlePlusId;

    @ManyToMany
    public List<Member> links = new ArrayList<Member>();
    
    @OneToMany(mappedBy="member", cascade=CascadeType.ALL, orphanRemoval=true)
    public Set<Account> accounts = new HashSet<Account>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    public Set<Interest> interests = new TreeSet<Interest>();
   
    @ElementCollection
    public Set<Badge> badges = new HashSet<Badge>();
            
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
     * @param login Login to find
     * @return Member found, null if none.
     */
    public static Member findByLogin(final String login) {
        Member member = null;
        try {
            member = (Member) em()
                    .createNamedQuery("MemberByLogin")
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (NoResultException ex) {
            member = null;
        }
        return member;
    }
    
    public static void addLink(String login, String loginToLink) {
        Member member = Member.findByLogin(login);
        Member memberToLink = Member.findByLogin(loginToLink);
        member.links.add(memberToLink);
        member.save();
    }

    public static void removeLink(String login, String loginToLink) {
        Member member = Member.findByLogin(login);
        Member memberToLink = Member.findByLogin(loginToLink);
        member.links.remove(memberToLink);
        member.save();
    }

    public static boolean isLinkedTo(String login, String loginToLink) {
        Member member = Member.findByLogin(login);
        Member memberToLink = Member.findByLogin(loginToLink);
        return member.links.contains(memberToLink);
    }

    public List<Member> linkers() {
        return Member.find("select m from Member m, in (m.links) as l where l.id = ?", id).fetch();
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

    public static List<Member> findMembersInterestedBy(String interest) {
        return Member.find(
                "select distinct m from Member m join m.interests as i where i.name = ?", interest).fetch();
    }

    public static List<Member> findMembersInterestedBy(String... interests) {
        return Member.find(
                "select distinct m from Member m join m.interests as i "
                + "where i.name in (:interests) group by m having count(i.id) = :size").bind("interests", interests).bind("size", interests.length).fetch();
    }
    
    public void addBadge(Badge badge) {
        this.badges.add(badge);
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
                .append(this.login, other.login)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.login)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "login {" + login + "}, displayName {" + displayName + "}";
    }
}