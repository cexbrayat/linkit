package helpers.oauth;

import java.util.Collection;
import java.util.List;
import models.Account;
import models.auth.OAuthAccount;
import models.activity.StatusActivity;
import org.scribe.oauth.OAuthService;

/**
 * Interface for an OAuth provider, enabling authentication and authenticated REST resource fetching
 * @author Sryl <cyril.lacote@gmail.com>
 */
public interface OAuthProvider {
    
    /**
     * @param callbackUrl Callback URL for OAuth redirection after successful authorization
     * @return OAuth service
     */
    OAuthService getService();

    /**
     * Fetch user profile from provider
     * @param token valid OAuth access token
     * @param secret valid OAuth secret
     * @return Account fetched
     */
    OAuthAccount getUserAccount(String token, String secret);

    /**
     * Fetch secured REST resource from provider, with given valid OAuth credentials
     * @param URL REST resource's URL to GET (eventually asynchronously)
     * @param token valid OAuth access token
     * @param secret valid OAuth
     * @return HTTP response's body
     */
    String get(String URL, String token, String secret);

    OAuthAccount getEmptyUserAccount(String oauth_login);
}
