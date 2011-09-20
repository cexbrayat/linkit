package controllers;

import play.Logger;
import play.Play;
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
        // TWITTER is a OAuth.ServiceInfo object
        // getUser() is a method returning the current user 
        if (OAuth.isVerifierResponse()) {
            // We got the verifier; 
            // now get the access tokens using the request tokens
            final String token = flash.get(TOKEN_KEY);
            final String secret = flash.get(SECRET_KEY);
            OAuth.Response resp = OAuth.service(Providers.TWITTER).retrieveAccessToken(
                    token, secret);
            // let's store them and go back to index
            if (resp != null && resp.error == null) {
                session.put(TOKEN_KEY, resp.token);
                session.put(SECRET_KEY, resp.secret);
                Application.index();
            } else {
                Logger.error("Authentification impossible");
                if (resp != null) {
                    Logger.error(provider + " replied " + resp.error.details());
                }
                flash.error("Authentification impossible");
                index();
            }
        }
        
        OAuth twitt = OAuth.service(Providers.TWITTER);
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
    
    static public class Providers {

        public static final OAuth.ServiceInfo TWITTER = loadConfiguration("twitter");

        static OAuth.ServiceInfo loadConfiguration(final String provider) {
            final String requestTokenURL = Play.configuration.getProperty(provider+".requestTokenUrl");
            final String accessTokenURL = Play.configuration.getProperty(provider+".accessTokenUrl");
            final String authorizeURL = Play.configuration.getProperty(provider+".authorizeUrl");
            final String consumerKey = Play.configuration.getProperty(provider+".consumerKey");
            final String consumerSecret = Play.configuration.getProperty(provider+".consumerSecret");
            return new OAuth.ServiceInfo(requestTokenURL, accessTokenURL, authorizeURL, consumerKey, consumerSecret);
        }
    }

}
