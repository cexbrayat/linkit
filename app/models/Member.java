package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Member extends Model {

    public String email;
    public String firstname;
    public String lastname;
    public String description;
    public String login;
    @ManyToMany
    public List<Member> links = new ArrayList<Member>();
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
        interests.add(Interest.findOrCreateByName(interest));
        return this;
    }

    public Member addInterests(String... interests) {
        for (String name : interests) {
                this.interests.add(Interest.findOrCreateByName(name));
        }
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