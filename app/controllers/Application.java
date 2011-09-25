package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;

import java.util.*;

import models.*;
import org.apache.commons.lang.StringUtils;

public class Application extends Controller {

    public static void index() {
        render();
    }

    public static void register(Member member) {
        List<Interest> interests = Interest.findAll();
        render("Application/register.html", member, interests);
    }

    public static void endRegistration(@Required String login, String firstname, String lastname, @Required @Email String email, @Required String displayName, @Required String description, String twitterName, String googlePlusId,
            String[] checkedInterests, String newInterests) {
        Logger.info("firstname {" + firstname + "}, lastname {" + lastname + "}, "
                + "email {" + email + "}, newInterests {" + newInterests + "}");

        Member member = Member.find("byLogin", login).first();
        member.firstname = firstname;
        member.description = description;
        member.email = email;
        member.lastname = lastname;
        member.login = login;
        member.displayName = displayName;
        member.twitterName = twitterName;
        member.googlePlusId = googlePlusId;

        if (validation.hasErrors()) {
            Logger.error(validation.errors().toString());
            List<Interest> interests = Interest.findAll();
            render("Application/register.html", member, interests);
        }
        if (checkedInterests != null) {
            member.addInterests(checkedInterests);
        }
        if (newInterests != null) {
            member.addInterests( StringUtils.splitByWholeSeparator(newInterests, ","));
        }
        member.save();
        flash.success("Profil enregistré!");
        Logger.info("Profil enregistré");

        showMember(member.login);
    }

    public static void showMembers() {
        List<Member> members = Member.findAll();
        Logger.info(Member.count() + " membres");
        render("Application/list.html", members);
    }
    
    public static void showMembersbyInterest(String interest) {
        List<Member> members = Member.findMembersInterestedBy(interest);
        Logger.info(Member.count() + " membres interested by "+interest);
        render("Application/list.html", members,interest);
    }

    public static void showMember(String login) {
        Logger.info("Profil " + login);
        Member member = Member.find("byLogin", login).first();
        Logger.info("Profil " + member);
        render("Application/profile.html", member);
    }

    public static void deleteMember(String login) throws Throwable {
        Logger.info("Delete Profile " + login);
        Member member = Member.find("byLogin", login).first();
        member.delete();
        Logger.info("Delete Profile " + login);
        flash.success("Votre compte a été supprimé");
        Secure.logout();
    }

    public static void link(String login, String loginToLink) {
        if (login == null || login.isEmpty()) {
            redirect("/secure/login");
        }
        Member.addLink(login, loginToLink);
        flash.success("Link ajouté!");
        showMember(loginToLink);
    }

    public static void unlink(String login, String loginToLink) {
        if (login == null || login.isEmpty()) {
            redirect("/secure/login");
        }
        Member.removeLink(login, loginToLink);
        flash.success("Link supprimé!");
        showMember(loginToLink);
    }
}