package controllers;

import com.google.common.collect.Maps;
import helpers.oauth.OAuthProvider;
import helpers.oauth.OAuthProviderFactory;
import java.util.Map;
import models.Member;
import models.ProviderType;
import models.auth.AuthAccount;
import models.auth.LinkItAccount;
import models.auth.OAuthAccount;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import play.Logger;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.libs.OAuth;
import play.mvc.Router;
import play.mvc.Router.ActionDefinition;

/**
 * OAuth Login controller
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class Login extends PageController {

    private static final String TOKEN_KEY = "token";
    private static final String SECRET_KEY = "secret";
    private static final String RETURN_URL = "url";

    /**
     * Displays available authentication methods
     *
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
                AuthAccount account = AuthAccount.find(providerType, oAuthAccount.getOAuthLogin());

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
//        Router.ActionDefinition ad = Router.reverse("Login.loginWith").add("provider", provider);
//        ad.absolute();
//        return ad.url;
        Map<String,Object> params = Maps.newHashMapWithExpectedSize(1);
        params.put("provider", provider);
        return Router.getFullUrl("Login.loginWith", params);
    }

    protected static void manageNewAuthenticationFrom(OAuthAccount oAuthAccount) {

        Member member = oAuthAccount.findCorrespondingMember();
        if (member == null) {
            // On crée un nouveau member, qu'on invitera à renseigner son profil
            member = new Member(oAuthAccount.getOAuthLogin());
            member.register(oAuthAccount);
            session.put("username", member.login);
            Profile.edit();
        } else {
            // Un membre existant s'est connecté avec un nouveau provider
            // On se contente de lui ajouter le nouvel account utilisé
            member.authenticate(oAuthAccount);
            member.updateProfile();
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
            // Redirect to dashboard
            Dashboard.index();
        }
    }

    public static void loginLinkIt(@Required String login, @Required String password) throws Throwable {
        flash.keep(RETURN_URL);
        Secure.authenticate(login, password, true);
        onSuccessfulAuthentication(login);
    }

    public static void signup(@Required String login, @Required String password) {
        if (Validation.hasErrors()) {
            render(login, password);
        }
        //unicite du login
        if (Member.findByLogin(login) != null) {
            flash.error("Ce login est déjà utilisé");
            render(login, password);
        }
        Member member = new Member(login);
        member.register(new LinkItAccount(password));
        session.put("username", member.login);
        Profile.edit();
    }
}
