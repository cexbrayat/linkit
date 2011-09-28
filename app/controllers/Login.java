package controllers;

import controllers.oauth.OAuthProvider;
import controllers.oauth.OAuthProviderFactory;
import models.LinkItAccount;
import models.Member;
import models.OAuthAccount;
import models.ProviderType;
import play.Logger;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.libs.OAuth;
import play.mvc.Controller;

/**
 * OAuth Login controller
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class Login extends Controller {

    private static final String TOKEN_KEY = "token";
    private static final String SECRET_KEY = "secret";

    public static void index() {
        render();
    }

    public static void loginWith(@Required String provider) {

        ProviderType providerType = ProviderType.valueOf(provider);
        OAuthProvider oauthProvider = OAuthProviderFactory.getProvider(providerType);

        if (OAuth.isVerifierResponse()) {
            // We got the verifier; 
            // now get the access tokens using the request tokens
            final String token = flash.get(TOKEN_KEY);
            final String secret = flash.get(SECRET_KEY);
            OAuth.Response resp = OAuth.service(oauthProvider.getServiceInfo()).retrieveAccessToken(token, secret);
            if (resp != null && resp.error == null) {
                session.put(TOKEN_KEY, resp.token);
                session.put(SECRET_KEY, resp.secret);

                // Fetch user oAuthAccount
                OAuthAccount oAuthAccount = oauthProvider.getUserAccount(resp.token, resp.secret);
                // Retrieve existing oAuthAccount from profile
                OAuthAccount account = (OAuthAccount) OAuthAccount.find(providerType, oAuthAccount.getOAuthLogin());

                if (account != null) {
                    session.put("username", account.member.login);
                    Profile.show(account.member.login);
                }

                // Pas d'account correspondant : new way of authentication
                manageNewAuthenticationFrom(oAuthAccount);
            } else {
                Logger.error("Authentification impossible");
                if (resp != null) {
                    Logger.error(provider + " replied " + resp.error.details());
                }
                flash.error("Authentification impossible");
                index();
            }
        }

        OAuth service = OAuth.service(oauthProvider.getServiceInfo());
        OAuth.Response resp = service.retrieveRequestToken();
        if (resp != null && resp.error == null) {
            // We received the unauthorized tokens 
            // we need to store them before continuing
            flash.put(TOKEN_KEY, resp.token);
            flash.put(SECRET_KEY, resp.secret);
            // Redirect the user to the authorization page
            redirect(service.redirectUrl(resp.token));
        } else {
            Logger.error("Authentification impossible");
            if (resp != null) {
                Logger.error(provider + " replied " + resp.error.details());
            }
            flash.error("Authentification impossible");
        }
    }

    protected static void manageNewAuthenticationFrom(OAuthAccount oAuthAccount) {

        Member member = oAuthAccount.findCorrespondingMember();
        if (member == null) {
            // On crée un nouveau member, qu'on invitera à renseigner son profil
            member = new Member(oAuthAccount.getOAuthLogin(), oAuthAccount);

            member.save();
            session.put("username", member.login);
            render("Profile/edit.html", member);
        } else {
            // Un membre existant s'est connecté avec un nouveau provider
            // On se contente de lui ajouter le nouvel account utilisé
            member.addAccount(oAuthAccount);
            // On valorise les éventuels données de son profil que 
            member.save();
            session.put("username", member.login);
            Profile.show(member.login);
        }
    }

    public static void loginLinkIt(@Required String login, @Required String password) throws Throwable {
        Secure.authenticate(login, password, true);
    }

    public static void signup(@Required String login, @Required String password) {
        if (Validation.hasErrors()) {
            render(login, password);
        }
        Member member = new Member(login, new LinkItAccount(password));
        member.save();
        session.put("username", member.login);
        render("Profile/edit.html", member);
    }
}
