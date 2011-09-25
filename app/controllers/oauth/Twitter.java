package controllers.oauth;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import models.OAuthAccount;
import models.ProviderType;
import models.TwitterAccount;
import play.libs.OAuth.ServiceInfo;
import play.libs.WS;

/**
 * A Twitter account
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class Twitter extends AbstractOAuthProviderImpl {

    static private final ServiceInfo serviceInfo = getServiceInfo(ProviderType.Twitter.name());

    public ServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    public OAuthAccount getUserAccount(String token, String secret) {
        
        JsonElement response = WS.url("http://api.twitter.com/1/account/verify_credentials.json")
                .oauth(getServiceInfo(), token, secret)
                .get()
                .getJson();
        JsonObject object = response.getAsJsonObject();
        
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
