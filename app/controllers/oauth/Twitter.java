package controllers.oauth;

import com.google.gson.JsonObject;
import models.OAuthAccount;
import models.ProviderType;
import models.TwitterAccount;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.oauth.OAuthService;

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
        final JsonObject object = getAsJsonObject(get(url, token, secret));
        
        TwitterAccount account = new TwitterAccount(token, secret);
        account.screenName = getStringPropertyFromJson(object, "screen_name");
        account.userId = getLongPropertyFromJson(object, "id");
        account.lang = getStringPropertyFromJson(object, "lang");
        account.name = getStringPropertyFromJson(object, "name");
        account.location = getStringPropertyFromJson(object, "location");
        account.profileImageUrl = getStringPropertyFromJson(object, "profile_image_url");
        account.statusesCount = getLongPropertyFromJson(object, "statuses_count");
        account.friendsCount = getLongPropertyFromJson(object, "friends_count");
        
        return account;
    }
}
