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
public class Twitter extends AbstractProvider {

    static private ServiceInfo serviceInfo;

    public ServiceInfo getServiceInfo() {
        if (serviceInfo == null) {
            serviceInfo = getServiceInfo(ProviderType.Twitter.name());
        }
        return serviceInfo;
    }

    public OAuthAccount getUserAccount(String token, String secret) {
        
        JsonElement response = WS.url("http://api.twitter.com/1/account/verify_credentials.json").oauth(getServiceInfo(), token, secret).get().getJson();
        JsonObject object = response.getAsJsonObject();
        TwitterAccount account = new TwitterAccount();
        account.token = token;
        account.secret = secret;
        account.screenName = getStringPropertyFromJson(object, "screen_name");
        account.userId = getLongPropertyFromJson(object, "id");
        return account;
    }
}
