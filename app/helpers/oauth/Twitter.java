package helpers.oauth;

import com.google.gson.JsonObject;
import helpers.JSON;
import models.auth.OAuthAccount;
import models.ProviderType;
import models.auth.TwitterOAuthAccount;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import play.exceptions.UnexpectedException;

/**
 * Twitter OAuth provider
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class Twitter extends AbstractOAuthProviderImpl {

    public Twitter() {
        super(ProviderType.Twitter);
    }

    @Override
    protected OAuthService buildService() {
         return new ServiceBuilder()
               .provider(TwitterApi.Authenticate.class)
               .apiKey(getConfigString("consumerKey"))
               .apiSecret(getConfigString("consumerSecret"))
               .callback(getCallbackUrl())
               .build();
    }

    public OAuthAccount getUserAccount(String token, String secret) {

        final String url = getConfigString("userProfileJsonUrl");
        final JsonObject object = JSON.getAsObject(get(url, token, secret));

        TwitterOAuthAccount account = new TwitterOAuthAccount(token, secret);
        account.screenName = JSON.getStringProperty(object, "screen_name");
        account.userId = JSON.getLongProperty(object, "id");
        account.lang = JSON.getStringProperty(object, "lang");
        account.name = JSON.getStringProperty(object, "name");
        account.location = JSON.getStringProperty(object, "location");
        account.profileImageUrl = JSON.getStringProperty(object, "profile_image_url");
        account.statusesCount = JSON.getLongProperty(object, "statuses_count");
        account.friendsCount = JSON.getLongProperty(object, "friends_count");

        return account;
    }

    public void postMessage(TwitterOAuthAccount account, String message) {
        try {
            String url = "http://api.twitter.com/1/statuses/update.json";
            OAuthRequest request = new OAuthRequest(Verb.POST, url);
            request.addQuerystringParameter("status", message);
            post(request, account.token, account.secret);
        } catch (Exception e) {
            throw new UnexpectedException(e);
        }
    }

    public void verifyCredentials(TwitterOAuthAccount account) {
        String url = "http://api.twitter.com/1/account/verify_credentials.json";
        OAuthRequest request = new OAuthRequest(Verb.GET, url);
        post(request, account.token, account.secret);
    }
}
