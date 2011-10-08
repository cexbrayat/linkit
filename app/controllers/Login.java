package controllers;

import controllers.oauth.OAuthProvider;
import controllers.oauth.OAuthProviderFactory;
import models.LinkItAccount;
import models.Member;
import models.OAuthAccount;
import models.ProviderType;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import play.Logger;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.libs.OAuth;
import play.mvc.Controller;
import play.mvc.Router;

/**
 * OAuth Login controller
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class Login extends Controller {

    private static final String TOKEN_KEY = "token";
    private static final String SECRET_KEY = "secret";
    private static final String RETURN_URL = "return";

    /**
     * Displays available authentication methods
     * @param url Optional URL to return to after successful login 
     */
    public static void index(String url) {
        if (url != null) {
            flash.put(RETURN_URL, url);
        }
        render();
    }

    public static void loginWith(@Required String provider) {

        flash.keep(RETURN_URL);
        ProviderType providerType = ProviderType.valueOf(provider);
        OAuthProvider oauthProvider = OAuthProviderFactory.getProvider(providerType);
        OAuthService oauthService = oauthProvider.getService();

        if (OAuth.isVerifierResponse()) {
            // We got the verifier; 
            // now get the access tokens using the request tokens
            final Token requestToken = new Token(flash.get(TOKEN_KEY), flash.get(SECRET_KEY));
            final String verifier = params.get("oauth_verifier");
            try {
                Token accessToken = oauthService.getAccessToken(requestToken, new Verifier(verifier));

                // Fetch user oAuthAccount
                OAuthAccount oAuthAccount = oauthProvider.getUserAccount(accessToken.getToken(), accessToken.getSecret());
                // Retrieve existing oAuthAccount from profile
                OAuthAccount account = (OAuthAccount) OAuthAccount.find(providerType, oAuthAccount.getOAuthLogin());

                if (account != null) {
                    onSuccessfulAuthentication(account.member.login);
                } else {

                    // Pas d'account correspondant : new way of authentication
                    manageNewAuthenticationFrom(oAuthAccount);
                }
            } catch (OAuthException ex) {
                Logger.error("Authentification impossible avec " + provider + " : " + ex.getLocalizedMessage());
                flash.error("Authentification impossible");
                index(null);
            }
        }

        try {
            Token token = oauthService.getRequestToken();
            // We received the unauthorized tokens 
            // we need to store them before continuing
            flash.put(TOKEN_KEY, token.getToken());
            flash.put(SECRET_KEY, token.getSecret());
            // Redirect the user to the authorization page
            redirect(oauthService.getAuthorizationUrl(token));
        } catch (OAuthException ex) {
            Logger.error("Authentification impossible avec " + provider + " : " + ex.getLocalizedMessage());
            flash.error("Authentification impossible");
            index(null);
        }
    }
    
    public static String getCallbackUrl(ProviderType provider) {
        Router.ActionDefinition ad = Router.reverse("Login.loginWith").add("provider", provider);
        ad.absolute();
        return ad.toString();
    }

    protected static void manageNewAuthenticationFrom(OAuthAccount oAuthAccount) {

        Member member = oAuthAccount.findCorrespondingMember();
        if (member == null) {
            // On crée un nouveau member, qu'on invitera à renseigner son profil
            member = new Member(oAuthAccount.getOAuthLogin(), oAuthAccount);
            member.register();
            session.put("username", member.login);
            render("Profile/edit.html", member);
        } else {
            // Un membre existant s'est connecté avec un nouveau provider
            // On se contente de lui ajouter le nouvel account utilisé
            member.addAccount(oAuthAccount);
            // On valorise les éventuels nouvelles infos de son profil obtenues par ce nouveau provider
            member.save();
            
            onSuccessfulAuthentication(member.login);
        }
    }

    protected static void onSuccessfulAuthentication(String login) {
        
        session.put("username", login);
                
        String returnUrl = flash.get(RETURN_URL);
        if (returnUrl != null) {
            // Return to origin URL
            flash.remove(RETURN_URL);
            redirect(returnUrl);
        } else {
            // Redirect to user profile
            Profile.show(login);
        }
    }
    
    public static void loginLinkIt(@Required String login, @Required String password) throws Throwable {
        Secure.authenticate(login, password, true);
        onSuccessfulAuthentication(login);
    }

    public static void signup(@Required String login, @Required String password) {
        if (Validation.hasErrors()) {
            render(login, password);
        }
        Member member = new Member(login, new LinkItAccount(password));
        member.register();
        session.put("username", member.login);
        render("Profile/edit.html", member);
    }
}
