package controllers.oauth;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import models.GoogleAccount;
import models.OAuthAccount;
import models.ProviderType;
import models.activity.StatusActivity;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.GoogleApi;
import org.scribe.oauth.OAuthService;
import play.Logger;

/**
 * Google OAuth provider
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class Google extends AbstractOAuthProviderImpl {

    //2011-10-04T14:41:40.837Z
    static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    
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

    public List<StatusActivity> fetchActivities(OAuthAccount account) {
        List<StatusActivity> statuses = new ArrayList<StatusActivity>();
        
        StringBuilder url = new StringBuilder("https://www.googleapis.com/plus/v1/people/")
                .append(account.member.googlePlusId)
                .append("/activities/public?key=AIzaSyC4xOkQsEPJcUKUvQGL6T7RZkrIIxSuZAg");
        JsonObject response = getAsJsonObject(get(url.toString()));    // Unauthenticated
        JsonArray activities = response.get("items").getAsJsonArray();
        DateFormat googleFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        for (JsonElement element : activities) {
            JsonObject activity = element.getAsJsonObject();
            try {
                String content = activity.get("object").getAsJsonObject().get("content").getAsString();
                Date date = googleFormatter.parse(activity.get("published").getAsString());
                String statusId = activity.get("id").getAsString();
                // FIXME Twitter statusUrl
                String statusUrl = activity.get("url").getAsString();
                statuses.add(new StatusActivity(account.member, date, account.provider, content, statusUrl, statusId));
            } catch (ParseException pe) {
                Logger.error("ouch! parse exception " + pe.getMessage());
            }
        }
        return statuses;
     }
}
