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
                
                // Retrieve user oAuthAccount
                OAuthAccount oAuthAccount = oauthProvider.getUserAccount(resp.token, resp.secret);
                // Retrieve Link-IT oAuthAccount from profile
                OAuthAccount account = (OAuthAccount) OAuthAccount.find(providerType, oAuthAccount.getOAuthLogin());

                if (account == null) {
                    // Pas d'account correspondant.
                    // Si on n'autorise pas de connexions par un provider différent, cela veut dire qu'il n'existe pas de member correspondant.

                    // On crée un nouveau member, qu'on invitera à renseigner son profil
                    Member member = new Member(oAuthAccount.getOAuthLogin(), oAuthAccount);
                    
                    // On préinitialise son profil avec les données récupérées du compte OAuth
                    oAuthAccount.initMemberProfile();
     
                    member.save();
                    session.put("username", member.login);
                    Application.register(member);
                }
                
                session.put("username", account.member.login);
                Application.showMember(account.member.login);
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
        Application.register(member);
    }
}
