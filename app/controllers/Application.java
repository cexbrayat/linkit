package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }
   
    public static void register(Member membre) {
        render(membre);
    }

    public static void register() {
        render();
    }

    // FIXME Manage Link-IT/Twitter/Google account
    public static void endRegistration(@Required String firstname, @Required String lastname, @Required @Email String email, @Required String description, String login, String password) {
        Logger.info("firstname {" + firstname + "}, lastname {" + lastname + "}, email {" + email + "}");
        if (validation.hasErrors()) {
            Logger.error(validation.errors().toString());
            register();            
        }
        Member member = Member.find("byLogin", login).first();
        // FIXME Separate profile edition (without login/password) from registration (first and only typing of login/password for LinkIt authentication)
        if(member == null){
            LinkItAccount account = new LinkItAccount();
            account.password = password;
            member = new Member(login, account);
        }
        member.firstname = firstname;
        member.description = description;
        member.email = email;
        member.lastname = lastname;
        member.login = login;
        
        session.put("username", login);
        member.save();
        flash.success("Profil enregistré!");
        Logger.info("Profil enregistré");
        
        showMember(member.login);
    }
    
    public static void showMembers(){
        List<Member> members = Member.findAll();
        Logger.info(Member.count() + " membres");
        render("Application/list.html", members);
    }
    
    public static void showMember(String login){
        Logger.info("Profil " + login);
        Member member = Member.find("byLogin", login).first();
        Logger.info("Profil " + member);
        render("Application/profile.html", member);
    }
    
    public static void link(String login, String loginToLink) {
        if(login == null || login.isEmpty()){
            redirect("/secure/login");
        }
        Member.addLink(login, loginToLink);
        flash.success("Link ajouté!");
        showMember(loginToLink);
    }
    
    public static void unlink(String login, String loginToLink) {
        if(login == null || login.isEmpty()){
            redirect("/secure/login");
        }
        Member.removeLink(login, loginToLink);
        flash.success("Link supprimé!");
        showMember(loginToLink);
    }
}