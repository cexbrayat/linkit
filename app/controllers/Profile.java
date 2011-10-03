package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;

import java.util.*;

import models.*;
import org.apache.commons.lang.StringUtils;

public class Profile extends Controller {

    public static void edit(@Required String login) {
        Logger.info("Profil " + login);
        Member member = Member.findByLogin(login);
        Logger.info("Edition du profil " + member);
        render(member);
    }

    public static void save(@Required String login, String firstname, String lastname, @Required @Email String email, @Required String displayName, @Required String description, String twitterName, String googlePlusId,
            String[] interests, String newInterests) {
        Logger.info("firstname {" + firstname + "}, lastname {" + lastname + "}, "
                + "email {" + email + "}, newInterests {" + newInterests + "}");

        Member member = Member.findByLogin(login);
        member.firstname = firstname;
        member.description = description;
        member.email = email;
        member.lastname = lastname;
        member.login = login;
        member.displayName = displayName;
        member.twitterName = twitterName;
        member.googlePlusId = googlePlusId;
        if (interests != null) {
            member.updateInterests(interests);
        }

        if (validation.hasErrors()) {
            Logger.error(validation.errors().toString());
            render("Profile/edit.html", member, newInterests);
        }

        if (newInterests != null) {
            member.addInterests( StringUtils.splitByWholeSeparator(newInterests, ","));
        }

        member.updateProfile();
        flash.success("Profil enregistré!");
        Logger.info("Profil enregistré");

        show(member.login);
    }

    public static void show(String login) {
        Logger.info("Profil " + login);
        Member member = Member.fetchForProfile(login);
        Logger.info("Profil " + member);
        render(member);
    }

    public static void delete(String login) throws Throwable {
        Logger.info("Delete Profile " + login);
        Member member = Member.findByLogin(login);
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
        show(loginToLink);
    }

    public static void unlink(String login, String loginToLink) {
        if (login == null || login.isEmpty()) {
            redirect("/secure/login");
        }
        Member.removeLink(login, loginToLink);
        flash.success("Link supprimé!");
        show(loginToLink);
    }
}