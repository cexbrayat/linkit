package controllers;

import models.*;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.data.validation.Email;
import play.data.validation.Required;

public class Profile extends PageController {
    
    public static void edit() {
        Member member = Member.findByLogin(Security.connected());
        Logger.info("Edition du profil " + member);
        render(member);
    }

    public static void save(@Required Long id, @Required String login, String firstname, String lastname, @Required @Email String email, @Required String description, String twitterName, String googlePlusId,
                            String[] interests, String newInterests) {
        Logger.info("Save Profile login {" + login + "}, firstname {" + firstname + "}, lastname {" + lastname + "}, "
                + "email {" + email + "}, newInterests {" + newInterests + "}");

        Member member = Member.findById(id);
        member.login = login;
        member.firstname = firstname;
        member.description = description;
        member.email = email;
        member.lastname = lastname;
        
        TwitterAccount twitter = member.getTwitterAccount();
        if (StringUtils.isNotBlank(twitterName)) {
            if (twitter == null) {
                member.addAccount(new TwitterAccount(twitterName));
            } else {
                twitter.screenName = twitterName;
            }
        } else {
            if (twitter != null) {
                member.removeAccount(twitter);
            }
        }
        
        GoogleAccount google = member.getGoogleAccount();
        if (StringUtils.isNotBlank(googlePlusId)) {
            if (google == null) {
                member.addAccount(new GoogleAccount(googlePlusId));
            } else {
                google.googleId = googlePlusId;
            }
        } else {
            if (google != null) {
                member.removeAccount(google);
            }
        }

        if (interests != null) {
            member.updateInterests(interests);
        }

        if (newInterests != null) {
            member.addInterests(StringUtils.splitByWholeSeparator(newInterests, ","));
        }

        Member other = Member.findByLogin(login);
        if (other != null && !member.equals(other)) {
            validation.addError("login", "validation.login.unique", login);
        }
        
        if (validation.hasErrors()) {
            Logger.error(validation.errors().toString());
            render("Profile/edit.html", member, newInterests);
        }

        session.put("username", member.login);
        member.updateProfile();
        flash.success("Profil enregistré!");
        Logger.info("Profil enregistré");

        show(member.login);
    }

    public static void show(String login) {
        Member member = Member.fetchForProfile(login);
        member.lookedBy(Member.findByLogin(Security.connected()));
        Logger.info("Profil " + member);
        render(member);
    }

    public static void delete() throws Throwable {
        Member member = Member.findByLogin(Security.connected());
        member.delete();
        Logger.info("Deleted profile " + member);
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