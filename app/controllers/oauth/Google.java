package controllers.oauth;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import models.GoogleAccount;
import models.OAuthAccount;
import models.ProviderType;
import play.libs.OAuth.ServiceInfo;
import play.libs.WS;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class Google extends AbstractProvider {

    static private ServiceInfo serviceInfo;

    public ServiceInfo getServiceInfo() {
        if (serviceInfo == null) {
            serviceInfo = getServiceInfo(ProviderType.Google.name());
        }
        return serviceInfo;
    }

    public OAuthAccount getUserAccount(String token, String secret) {
        
        // FIXME Google profile information
        JsonElement response = WS.url("https://www.googleapis.com/oauth2/v1/userinfo?alt=json").oauth(getServiceInfo(), token, secret).get().getJson();
        JsonObject object = response.getAsJsonObject();

        GoogleAccount account = new GoogleAccount();
        account.googleId = getStringPropertyFromJson(object, "id");
        account.name = getStringPropertyFromJson(object, "name");
        account.givenName = getStringPropertyFromJson(object, "given_name");
        account.familyName = getStringPropertyFromJson(object, "family_name");
        account.link = getStringPropertyFromJson(object, "link");
        account.picture = getStringPropertyFromJson(object, "picture");
        account.gender = getStringPropertyFromJson(object, "gender");
        account.birthday = getStringPropertyFromJson(object, "birthday");
        account.locale = getStringPropertyFromJson(object, "locale");
       
        return account;
    }
}
