package controllers;

import com.google.common.collect.Maps;
import helpers.oauth.OAuthProvider;
import helpers.oauth.OAuthProviderFactory;

import java.util.HashMap;
import java.util.Map;

import models.Account;
import models.Member;
import models.ProviderType;
import models.auth.AuthAccount;
import models.auth.LinkItAccount;
import models.auth.OAuthAccount;
import models.mailing.Mailing;
import org.apache.commons.lang.StringUtils;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import play.Logger;
import play.cache.Cache;
import play.data.validation.Email;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.i18n.Messages;
import play.libs.Codec;
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

    //private static final Map<String, String> passwordResetRequests = new HashMap<String, String>();

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

    public static void loginWith(@Required ProviderType provider) {

        flash.keep(RETURN_URL);

        if (provider == null) {
            flash.error("Mauvaise requète, le provider d'authentification n'est pas indiqué");
            index(null);
        }

        OAuthProvider oauthProvider = OAuthProviderFactory.getProvider(provider);
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
                AuthAccount account = AuthAccount.find(provider, oAuthAccount.getOAuthLogin());

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
            member.authenticate(oAuthAccount);
            member.updateProfile(false);
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

    public static void noNetwork() {
        if (session.get("username") != null) {
            flash.success(Messages.get("login.already.logged-in"));
            Dashboard.index();
        }
        renderTemplate("Login/linkit.html");
    }

    public static void loginLinkIt(@Required String login, @Required String password) throws Throwable {
        flash.keep(RETURN_URL);

        if (Validation.hasErrors()) {
            params.flash("login");
            flash.error("login & password requis");
            noNetwork();
        }

        if (Security.authenticate(login, password)) {
            onSuccessfulAuthentication(login);
        } else {
            params.flash("login");
            flash.error("Login/password invalides");
            noNetwork();
        }
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

    /**
     * Go to the password reset page.
     * There the email can be set by the user.
     */
    public static void goToRequestPasswordReset() {
        if (session.get("username") != null) {
            flash.success(Messages.get("login.already.logged-in"));
            Dashboard.index();
        }
        renderTemplate("Login/requestPasswordReset.html");
    }

    /**
     * Once the email is set, find it in db and send a mail
     * containing a link that will trigger Login.newPassword()
     *
     * @param email
     */
    public static void requestPasswordReset(@Email @Required String email) {
        if (Validation.hasErrors()) {
            render(email);
        }

        if (Member.findByEmail(email) == null) {
            Validation.addError("email", Messages.get("login.email.unknown"));
            render(email);
        }

        String passwordResetCode = Codec.UUID();
        Cache.add(passwordResetCode, email, "1h");

        //TODO : url in mail variabilisée ?
        Mails.resetPasswordLink(email, passwordResetCode);

        flash.success(Messages.get("login.password.lost.mail-sent"));
        Application.index();
    }

    public static void newPassword(String uuid) {
        checkUUID(uuid);
        renderTemplate("Login/newPassword.html", uuid);
    }

    public static void doResetPassword(@Required String password, @Required String password_check, @Required String uuid) {
        String email = checkUUID(uuid);

        if (Validation.hasErrors()) {
            renderTemplate("Login/newPassword.html", uuid);
        }

        if (!password.equals(password_check)) {
            Validation.addError("password", Messages.get("login.password.lost.password-check-failed"));
            renderTemplate("Login/newPassword.html", uuid);
        }

        Member member = Member.findByEmail(email);
        LinkItAccount account = null;
        if (member != null) {
            account = (LinkItAccount) LinkItAccount.find(ProviderType.LinkIt, member.login);
        }

        if (account == null) {
            Validation.addError("password", Messages.get("login.password.lost.no-LinkItAccount"));
            renderTemplate("Login/newPassword.html", uuid);
        } else {
            account.updatePassword(password);
        }

        Cache.delete(uuid);
        flash.success(Messages.get("login.password.lost.password-changed"));
        onSuccessfulAuthentication(member.login);
    }

    private static String checkUUID(String uuid) {
        String email = (String) Cache.get(uuid);

        if (email == null) {
            flash.error(Messages.get("login.password.lost.unknown-uuid"));
            Application.index();
        }

        return email;
    }
}
