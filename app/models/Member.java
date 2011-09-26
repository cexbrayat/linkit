package models;

import java.util.*;
import javax.persistence.*;

import org.apache.commons.lang.StringUtils;
import play.db.jpa.*;

@Entity
public class Member extends Model {

    /** Internal login */
    public String login;
    
    public String email;
    public String firstname;
    public String lastname;
    
    /** Name under which he wants to be displayed */
    public String displayName;

    /** User-defined description, potentially as MarkDown */
    public String description;
    
    /** Twitter account interet */
    public String twitterName;
    
    /** Google+ ID, i.e https://plus.google.com/{ThisFuckingLongNumberInsteadOfABetterId} as seen on Google+' profile link */
    public String googlePlusId;

    @ManyToMany
    public List<Member> links = new ArrayList<Member>();
    
    // FIXME : refactor to OneToMany (several accounts per member)
    @OneToOne(mappedBy="member", cascade= CascadeType.ALL)
    public Account account;

    @ManyToMany(cascade = CascadeType.PERSIST)
    public Set<Interest> interests = new TreeSet<Interest>();

    
    public Member(String login, Account account) {
        this.login = login;
        this.account = account;
        this.account.member = this;
    }

    public static void addLink(String login, String loginToLink) {
        Member member = Member.find("byLogin", login).first();
        Member memberToLink = Member.find("byLogin", loginToLink).first();
        member.links.add(memberToLink);
        member.save();
    }

    public static void removeLink(String login, String loginToLink) {
        Member member = Member.find("byLogin", login).first();
        Member memberToLink = Member.find("byLogin", loginToLink).first();
        member.links.remove(memberToLink);
        member.save();
    }

    public static boolean isLinkedTo(String login, String loginToLink) {
        Member member = Member.find("byLogin", login).first();
        Member memberToLink = Member.find("byLogin", loginToLink).first();
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

    public String toString() {
        return "login {" + login + "}, links {" + links.size() + "}";
    }
}