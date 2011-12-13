package controllers;

import java.util.ArrayList;
import java.util.List;
import models.*;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.data.validation.Email;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.data.validation.Validation.ValidationResult;

public class Profile extends PageController {
    
    public static void edit() {
        Member member = Member.findByLogin(Security.connected());
        Logger.info("Edition du profil " + member);
        List<SharedLink> sharedLinks = member.sharedLinks;
        render(member, sharedLinks);
    }

    public static void save(@Required Long id, @Required String login, String firstname, String lastname, String company, @Required @Email String email, @Required @MaxSize(140) String shortDescription, String longDescription, String twitterName, String googlePlusId,
                            String[] interests, String newInterests,
                            List<SharedLink> sharedLinks) {
        Logger.info("Save Profile login {" + login + "}, firstname {" + firstname + "}, lastname {" + lastname + "}, "
                + "email {" + email + "}, newInterests {" + newInterests + "}");

        Member member = Member.findById(id);
        member.login = login;
        member.firstname = firstname;
        member.shortDescription = shortDescription;
        member.longDescription = longDescription;
        member.email = email;
        member.lastname = lastname;
        member.company = company;
        
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

        List<SharedLink> validatedSharedLinks = new ArrayList<SharedLink>(sharedLinks.size());
        for (int i = 0; i < sharedLinks.size(); i++) {
            SharedLink link = sharedLinks.get(i);
            if (StringUtils.isNotBlank(link.name) && StringUtils.isNotBlank(link.URL)) {
                ValidationResult result = validation.valid("sharedLinks["+i+"]", link);
                if (result.ok) {
                    validatedSharedLinks.add(link);
                }
            }
        }
        member.updateSharedLinks(validatedSharedLinks);

        Member other = Member.findByLogin(login);
        if (other != null && !member.equals(other)) {
            validation.addError("login", "validation.login.unique", login);
        }

        if (validation.hasErrors()) {
            Logger.error(validation.errors().toString());
            render("Profile/edit.html", member, newInterests, sharedLinks);
        }

        session.put("username", member.login);
        member.updateProfile();
        flash.success("Profil enregistré!");
        Logger.info("Profil enregistré");

        show(member.login);
    }

    public static void show(String login) {
// FIXME CLA Not using fetchForProfile
//      Member member = Member.fetchForProfile(login);
        Member member = Member.findByLogin(login);
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