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
import models.OAuthAccount;
import models.ProviderType;
import models.TwitterAccount;
import models.activity.StatusActivity;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import play.Logger;
import play.exceptions.UnexpectedException;

/**
 * Twitter OAuth provider
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class Twitter extends AbstractOAuthProviderImpl {

    //Wed Oct 05 12:42:55 +0000 2011
    static final String DATE_FORMAT = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";

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

    public void postMessage(TwitterAccount account, String message) {
        try {
            String url = "http://api.twitter.com/1/statuses/update.json";
            OAuthRequest request = new OAuthRequest(Verb.POST, url);
            request.addQuerystringParameter("status", message);
            post(request, account.token, account.secret);
        } catch (Exception e) {
            throw new UnexpectedException(e);
        }
    }

    public void verifyCredentials(TwitterAccount account) {
        String url = "http://api.twitter.com/1/account/verify_credentials.json";
        OAuthRequest request = new OAuthRequest(Verb.GET, url);
        post(request, account.token, account.secret);
    }

    public List<StatusActivity> fetchActivities(OAuthAccount account) {
        List<StatusActivity> statuses = new ArrayList<StatusActivity>();
        
        StringBuilder url = new StringBuilder("https://api.twitter.com/1/statuses/user_timeline.json?include_rts=true");
        url.append("&screen_name=").append(account.member.twitterName);
        if (account.lastStatusId != null) {
            url.append("&since_id=").append(account.lastStatusId);
        }
        JsonElement response = getAsJsonElement(get(url.toString()));    // Unauthenticated
        JsonArray array = response.getAsJsonArray();
        DateFormat twitterFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        for (JsonElement element : array) {
            JsonObject tweet = element.getAsJsonObject();
            try {
                String content = tweet.get("text").getAsString();
                //TODO remplacer les username par les liens twitter (voire linkit si le profil existe).
                //Idem pour g+, renvoyer directement sur le profil linkit
                //Pattern p = Pattern.compile("@([A-Za-z0-9_]+)");
                //Matcher m = p.matcher(content);
                //while (m.find()) content.replace(m.group(1), "@<a href=\"http://www.twitter.com/" + m.group(1) + ">" + m.group(1) + "</a>");
                Date date = twitterFormatter.parse(tweet.get("created_at").getAsString());
                String statusId = tweet.get("id_str").getAsString();
                String statusUrl = "http://www.twitter.com/"+account.member.twitterName+"/status/"+statusId;
                statuses.add(new StatusActivity(account.member, date, account.provider, content, statusUrl, statusId));
            } catch (ParseException pe) {
                Logger.error("ouch! parse exception " + pe.getMessage());
            }
        }
        return statuses;
    }
}
