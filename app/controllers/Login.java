package controllers;

import controllers.oauth.Provider;
import controllers.oauth.ProviderFactory;
import models.Account;
import models.Member;
import play.Logger;
import play.data.validation.Required;
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
    
    public static void login(@Required String provider) {
        // getUser() is a method returning the current user 
        
        Provider prov = ProviderFactory.getProvider(provider);
        
        if (OAuth.isVerifierResponse()) {
            // We got the verifier; 
            // now get the access tokens using the request tokens
            final String token = flash.get(TOKEN_KEY);
            final String secret = flash.get(SECRET_KEY);
            OAuth.Response resp = OAuth.service(prov.getServiceInfo()).retrieveAccessToken(token, secret);
            if (resp != null && resp.error == null) {
                session.put(TOKEN_KEY, resp.token);
                session.put(SECRET_KEY, resp.secret);
                
                // Retrieve user account
                Account account = prov.getUserAccount(resp.token, resp.secret);
                // Retrieve Link-IT member from profile
                Member membre = Member.connectFromAccount(account);

                // FIXME : ne pas s'enregister automatiquement mais diriger vers l'enregistrement
                if (membre == null) {
                    membre = new Member(account);
                } else {
                    membre.account = account;
                }
                membre.save();
                
                Application.showMember(membre.login);
            } else {
                Logger.error("Authentification impossible");
                if (resp != null) {
                    Logger.error(provider + " replied " + resp.error.details());
                }
                flash.error("Authentification impossible");
                index();
            }
        }
        
        OAuth twitt = OAuth.service(prov.getServiceInfo());
        OAuth.Response resp = twitt.retrieveRequestToken();
        if (resp != null && resp.error == null) {
            // We received the unauthorized tokens 
            // we need to store them before continuing
            flash.put(TOKEN_KEY, resp.token);
            flash.put(SECRET_KEY, resp.secret);
            // Redirect the user to the authorization page
            redirect(twitt.redirectUrl(resp.token));
        } else {
            Logger.error("Authentification impossible");
            if (resp != null) {
                Logger.error(provider + " replied " + resp.error.details());
            }
            flash.error("Authentification impossible");
        }
    }
}
