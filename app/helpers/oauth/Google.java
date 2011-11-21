package helpers.oauth;

import com.google.gson.JsonObject;
import helpers.JSON;
import models.GoogleOAuthAccount;
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
        final JsonObject object = JSON.getAsObject(get(url, token, secret));

        GoogleOAuthAccount account = new GoogleOAuthAccount(token,secret);
        account.googleId = JSON.getStringProperty(object, "id");
        account.email = JSON.getStringProperty(object, "email");
        account.name = JSON.getStringProperty(object, "name");
        account.givenName = JSON.getStringProperty(object, "given_name");
        account.familyName = JSON.getStringProperty(object, "family_name");
        account.link = JSON.getStringProperty(object, "link");
        account.picture = JSON.getStringProperty(object, "picture");
        account.gender = JSON.getStringProperty(object, "gender");
        account.birthday = JSON.getStringProperty(object, "birthday");
        account.locale = JSON.getStringProperty(object, "locale");
 
        return account;
    }
}
