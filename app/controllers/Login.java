package controllers;

import com.google.common.collect.Maps;
import helpers.oauth.OAuthProvider;
import helpers.oauth.OAuthProviderFactory;

import java.util.Map;

import models.Member;
import models.ProviderType;
import models.auth.AuthAccount;
import models.auth.GoogleOAuthAccount;
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
        Logger.info("\n\nLoginWith");

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
                Logger.info("\n\nlogin " + providerType + " : " + oAuthAccount.getOAuthLogin() + "\n\n");
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

    public static void login(String oauth_provider, String oauth_login) {
        ProviderType providerType = ProviderType.valueOf(oauth_provider);
        String decryptedLogin = decrypt(oauth_login);
        AuthAccount account = AuthAccount.find(providerType, decryptedLogin);

        if (account != null) {
            Logger.info("[Login]Account found");
            onSuccessfulAuthentication(account.member.login);
        } else {
            Logger.info("[Login]Account not found for " + decryptedLogin);
            // Pas d'account correspondant : new way of authentication
            OAuthProvider oauthProvider = OAuthProviderFactory.getProvider(providerType);
            OAuthAccount gAccount = oauthProvider.getEmptyUserAccount(decryptedLogin);
            manageNewAuthenticationFrom(gAccount);
        }

    }

    public static String getCallbackUrl(ProviderType provider) {
//        Router.ActionDefinition ad = Router.reverse("Login.loginWith").add("provider", provider);
//        ad.absolute();
//        return ad.url;
        Map<String, Object> callbackParams = Maps.newHashMapWithExpectedSize(1);
        callbackParams.put("provider", provider);
        return Router.getFullUrl("Login.loginWith", callbackParams);
    }

    protected static void manageNewAuthenticationFrom(OAuthAccount oAuthAccount) {

        Member member = oAuthAccount.findCorrespondingMember();
        if (member == null) {
            // On crée un nouveau member, qu'on invitera à renseigner son profil
            member = new Member(oAuthAccount.getOAuthLogin());
            member.preregister(oAuthAccount);
            Profile.register(member.login, oAuthAccount.provider);
        } else {
            // Un membre existant s'est connecté avec un nouveau provider
            // On se contente de lui ajouter le nouvel account utilisé
            Logger.info("[manageNewAuthenticationFrom]Member found");
            member.authenticate(oAuthAccount);
            member.updateProfile();
            onSuccessfulAuthentication(member.login);
        }
    }

    protected static void onSuccessfulAuthentication(String login) {

        session.put("username", login);
        Logger.info("Logged : " + login);

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
        Secure.authenticate(login, password, true);
        onSuccessfulAuthentication(login);
    }

    public static void loginWithLinkIt(@Required String login, @Required String password) throws Throwable {
        String decryptedLogin = decrypt(login);
        String decryptedPassword = decrypt(password);
        loginLinkIt(decryptedLogin, decryptedPassword);
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
        member.preregister(new LinkItAccount(password));
        Profile.register(login, ProviderType.LinkIt);
    }

    private static String decrypt(String crypted) {
        int shiftKey = 5;
        String plainText = "";

        for (int i = 0; i < crypted.length(); i++) {
            int asciiValue = (int) crypted.charAt(i);
            if (asciiValue < 65 || (asciiValue > 90 && asciiValue < 97) || asciiValue > 122) {
                plainText += crypted.charAt(i);
                continue;
            }
            int newAsciiValue = -1000;
            if (asciiValue >= 97) {
                newAsciiValue = computeValue(asciiValue, 97, shiftKey);
            } else {
                newAsciiValue = computeValue(asciiValue, 65, shiftKey);
            }
            plainText += (char) newAsciiValue;

        }
        return plainText;
    }

    private static int computeValue(int asciiValue, int offset, int shiftKey) {
        int basicValue = asciiValue - offset;
        int newAsciiValue = -1000;
        if (basicValue - shiftKey < 0) {

            newAsciiValue = offset + 26 - (shiftKey - basicValue);
        } else {
            newAsciiValue = offset + (basicValue - shiftKey);
        }
        return newAsciiValue;
    }
}
