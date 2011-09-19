package models;
 
import java.util.*;
import javax.persistence.*;

import play.Logger; 
import play.db.jpa.*;
 
@Entity
public class Member extends Model {
 
    public String email;
    public String firstname;
    public String lastname;
    public String description;
    public String login;
    public String password;
    @OneToMany
    public List<Member> links = new ArrayList<Member>();
    
    public Member(String firstname, String lastname, String email, String description, String login, String password) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.description = description;
        this.login = login;
        this.password = password;
    }
    
    public static boolean connect(String login, String password){
        Member member = Member.find("byLogin", login).first();
        return (member != null && member.password.equals(password));
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
    
    public String toString(){
        return "login {" + login + "}, links {" + links.size() + "}";
    }
 
}