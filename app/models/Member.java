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
    
    public Member(String login, Account account) {
        this.login = login;
        account.member = this;
    }
    
    public static void addLink(String login, String loginToLink){
        Member member = Member.find("byLogin", login).first();
        Member memberToLink = Member.find("byLogin", loginToLink).first();
        member.links.add(memberToLink);
        member.save();
    }
    
    public static void removeLink(String login, String loginToLink){
        Member member = Member.find("byLogin", login).first();
        Member memberToLink = Member.find("byLogin", loginToLink).first();
        member.links.remove(memberToLink);
        member.save();
    }
    
    public static boolean isLinkedTo(String login, String loginToLink){
        Member member = Member.find("byLogin", login).first();
        Member memberToLink = Member.find("byLogin", loginToLink).first();
        return member.links.contains(memberToLink);
    }

    public List<Member> linkers(){
        return Member.find("select m from Member m, in (m.links) as l where l.id = ?", id).fetch();
    }
    
    @Override
    public String toString(){
        return "login {" + login + "}, links {" + links.size() + "}";
    }
 
}