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
    
    public static void register() {
        render("Application/register.html");
    }

    public static void endRegistration(@Required String firstname, @Required String lastname, @Required @Email String email, @Required String description, @Required String login, @Required String password) {
        Logger.info("firstname {" + firstname + "}, lastname {" + lastname + "}, email {" + email + "}");
        if (validation.hasErrors()) {
            Logger.error(validation.errors().toString());
            render("Application/register.html");            
        }
        Member member = Member.find("byEmail", email).first();
        if(member == null){
            member = new Member(firstname, lastname, email, description, login, password);
            member.save();
            flash.success("Profil enregistré!");
            Logger.info("Profil enregistré");
        }
        render("Application/profile.html", member);
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