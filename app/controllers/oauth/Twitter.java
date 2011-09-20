package controllers.oauth;

import com.google.gson.JsonElement;
import models.Account;
import play.libs.OAuth.ServiceInfo;
import play.libs.WS;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class Twitter extends AbstractProvider {

    static private ServiceInfo serviceInfo;
    
    static private final String PROVIDER_ID = "twitter";

    public ServiceInfo getServiceInfo() {
        if (serviceInfo == null) {
            serviceInfo = getServiceInfo(PROVIDER_ID);
        }
        return serviceInfo;
    }

    public Account getUserAccount(String token, String secret) {
        Account account = new Account(PROVIDER_ID);
        account.token = token;
        account.secret = secret;
        
        JsonElement response = WS.url("http://api.twitter.com/1/account/verify_credentials.json").oauth(getServiceInfo(), token, secret).get().getJson();
        account.login = getStringPropertyFromJson(response, "screen_name");
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
