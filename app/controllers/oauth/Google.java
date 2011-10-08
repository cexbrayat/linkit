package controllers.oauth;

import com.google.gson.JsonObject;
import models.GoogleAccount;
import models.OAuthAccount;
import models.ProviderType;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.GoogleApi;
import org.scribe.oauth.OAuthService;

/**
 * Google OAuth provider
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class Google extends AbstractOAuthProviderImpl {

    public Google() {
        super(ProviderType.Google);
    }

    @Override
    protected OAuthService buildService() {
         return new ServiceBuilder()
               .provider(GoogleApi.class)
               .apiKey(getConfigString("consumerKey"))
               .apiSecret(getConfigString("consumerSecret"))
               .callback(getCallbackUrl())
               .scope(getConfigString("scope"))
               .build();
    }

    public OAuthAccount getUserAccount(String token, String secret) {
        
        final String url = getConfigString("userProfileJsonUrl");
        final JsonObject object = getAsJsonObject(get(url, token, secret));

        GoogleAccount account = new GoogleAccount(token,secret);
        account.googleId = getStringPropertyFromJson(object, "id");
        account.email = getStringPropertyFromJson(object, "email");
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
