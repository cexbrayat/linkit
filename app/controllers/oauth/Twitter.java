package controllers.oauth;

import com.google.gson.JsonElement;
import models.OAuthAccount;
import models.ProviderType;
import models.TwitterAccount;
import play.libs.OAuth.ServiceInfo;
import play.libs.WS;

/**
 *
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
        
        TwitterAccount account = new TwitterAccount();
        account.token = token;
        account.secret = secret;
        account.screenName = getStringPropertyFromJson(response, "screen_name");
        account.userId = getLongPropertyFromJson(response, "id");
        return account;
    }
    
    static String getStringPropertyFromJson(JsonElement response, String property) {
        return response.getAsJsonObject().get(property).getAsString();
    }
    
    static Long getLongPropertyFromJson(JsonElement response, String property) {
        return response.getAsJsonObject().get(property).getAsLong();
    }
}
