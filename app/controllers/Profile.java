package controllers;

import models.*;
import models.validation.GoogleIDCheck;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.data.validation.*;
import play.i18n.Messages;
import play.mvc.With;
import play.templates.Template;
import play.templates.TemplateLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@With(SecureLinkIt.class)
public class Profile extends PageController {

    public static void edit() {
        Member member = Member.findByLogin(Security.connected());
        if (member == null) Login.index(request.url);

        Logger.info("Edition du profil " + member);
        String originalLogin = member.login;
        render(member, originalLogin);
    }

    public static void register(String login, ProviderType provider) {
        Member member = Member.getPreregistered(login);
        notFoundIfNull(login);

        Logger.info("Création du profil %s", member);
        String originalLogin = login;
        ProviderType registrationProvider = provider;
        render("Profile/edit.html", member, originalLogin, registrationProvider);
    }

    private static String cleanTwitterName(String twitterName) {
        String result = StringUtils.trim(twitterName);
        return StringUtils.remove(result, '@');
    }

    private static String cleanGoogleId(String googleId) {
        return StringUtils.trim(googleId);
    }

    private static String cleanSharedLinkURL(String url) {
        String result = StringUtils.trim(url);
        if (!StringUtils.startsWith(url, "http")) {
            result = "http://"+result;
        }
        return result;
    }
    
    public static void save(
            Long id,
            @Required String originalLogin,
            @Required @Match( value = Member.LOGIN_NOT_NUMERIC_PATTERN, message = "validation.notNumeric") String login,
            String firstname,
            @Required String lastname,
            String company,
            @Required @Email String email,
            @Required @MaxSize(140) String shortDescription, String longDescription,
            String twitterName, @CheckWith(GoogleIDCheck.class) String googlePlusId,
            String[] interests, String newInterests,
            List<SharedLink> sharedLinks) {
        Logger.info("Save Profile originalLogin {" + originalLogin + "}, firstname {" + firstname + "}, lastname {" + lastname + "}, "
                + "email {" + email + "}, newInterests {" + newInterests + "}");

        boolean registration = (id == null);
        Member member = null;
        if (registration) {
            member = Member.getPreregistered(originalLogin);
        } else {
            member = Member.findById(id);
        }
        notFoundIfNull(member);

        member.login = login;
        member.firstname = firstname;
        member.setShortDescription(shortDescription);
        member.setLongDescription(longDescription);
        member.email = email;
        member.lastname = lastname;
        member.company = company;


        twitterName = cleanTwitterName(twitterName);
        TwitterAccount twitter = member.getTwitterAccount();
        if (StringUtils.isNotBlank(twitterName)) {
            
            final Member other = TwitterAccount.findMemberByScreenName(twitterName);
            if (other != null && !member.equals(other)) {
                validation.addError("twitterName", "validation.unique", twitterName, other.toString());
            }

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

        googlePlusId = cleanGoogleId(googlePlusId);
        GoogleAccount google = member.getGoogleAccount();
        if (StringUtils.isNotBlank(googlePlusId)) {
            
            final Member other = GoogleAccount.findMemberByGoogleId(googlePlusId);
            if (other != null && !member.equals(other)) {
                validation.addError("googlePlusId", "validation.unique", googlePlusId, other.toString());
            }

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

        List<SharedLink> newSharedLinks = new ArrayList<SharedLink>(sharedLinks.size());
        for (SharedLink link : sharedLinks) {
            if (StringUtils.isNotBlank(link.name) || StringUtils.isNotBlank(link.URL)) {
                link.URL = cleanSharedLinkURL(link.URL);
                newSharedLinks.add(link);
            }
        }
        validation.valid("sharedLinks", newSharedLinks);

        // Login unicity
        Member other = Member.findByLogin(login);
        if (other != null && !member.equals(other)) {
            validation.addError("login", "validation.unique", login);
        }

        // Email unicity
        other = Member.findByEmail(email);
        if (other != null && !member.equals(other)) {
            validation.addError("email", "validation.unique", email);
        }

        // #198 firstname not mandatory for Sponsor
        if (!(member instanceof Sponsor)) {
            Validation.required("firstname", firstname);
        }

        if (validation.hasErrors()) {
            Logger.error(validation.errors().toString());
            flash.error(Messages.get("validation.errors"));
            // Set actual new sharedLinks on member, even if not validated, to serve values typed by user
            member.sharedLinks = newSharedLinks;
            render("Profile/edit.html", member, originalLogin, newInterests);
        }

        // Don't set new shared links on Member before being sure validation OK to avoid false new SharedLinkActivity
        member.updateSharedLinks(newSharedLinks);

        if (registration) {
            member.register(originalLogin);
        } else {
            member.updateProfile(true);
        }
        
        session.put("username", member.login);

        flash.success("Profil enregistré!");
        Logger.info("Profil %s enregistré", member.toString());

        show(member.login);
    }

    public static void show(String login) {
        Member member = Member.findByLogin(login);
        notFoundIfNull(member);

        checkProfileAccess(member);

        member.lookedBy(Member.findByLogin(Security.connected()));
        List<Talk> favorites = Vote.findFavoriteTalksByMemberOn(member, ConferenceEvent.CURRENT);
        Collections.shuffle(favorites);

        Logger.info("Show profil %s", member);
        render(member, favorites);
    }

    private static void checkProfileAccess(Member member) {
        Member connectedMember = Member.findByLogin(Security.connected());
        if (connectedMember == null && !member.publicProfile) {
            Logger.info("Try to display profile of %s without being connected", member);
            // redirect to login page
            Login.index(request.url);
        }
    }

    public static String link(Long memberId) {
        notFoundIfNull(memberId);
        Member member = Member.findById(memberId);
        notFoundIfNull(member);
        Member.addLink(Security.connected(), memberId);
        renderArgs.put("_arg", Member.findByLogin(Security.connected()));
        renderArgs.put("_short", true);
        Template template = TemplateLoader.load(template("tags/member.html"));
        return template.render(renderArgs.data);
    }

    public static void unlink(Long memberId) {
        notFoundIfNull(memberId);
        Member.removeLink(Security.connected(), memberId);
    }
    
    public static void delete() throws Throwable {
        Member member = Member.findByLogin(Security.connected());
        if (member == null) Login.index(null);
        render(member);
    }
    
    public static void confirmDelete() throws Throwable {
        Member member = Member.findByLogin(Security.connected());
        if (member == null) Login.index(null);

        if (member instanceof Staff) {
            flash.error("Désolé mec, on ne supprime pas les mecs du staff! Trop tard, on est dans le même bateau, on coule avec! Non plus sérieusement, c'est parce que tu es potentiellement auteur d'articles et lié à d'autres données qu'on ne peut pas supprimer...");
            show(member.login);
        } else {
            member.delete();
            Logger.info("Deleted profile %s", member);
            flash.success("Votre compte a été supprimé");
            Secure.logout();
        }
    }
}