package models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import helpers.JSON;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Entity;
import models.activity.StatusActivity;
import play.Logger;
import play.data.validation.Required;
import play.libs.WS;
import play.mvc.Router;

/**
 * A Twitter account
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class TwitterAccount extends Account {
    
    @Required
    public String screenName;

    //Wed Oct 05 12:42:55 +0000 2011
    static final String DATE_FORMAT = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
    
    public TwitterAccount() {
        super(ProviderType.Twitter);
    }
    
    @Override
    public String toString(){
        return "Twitter account @"+screenName;
    }
    
    public static Member findMemberByScreenName(final String screenName) {
        return TwitterAccount.find("select ta.member from TwitterAccount ta where ta.screenName=?", screenName).first();
    }

    public List<StatusActivity> fetchActivities() {
        List<StatusActivity> statuses = new ArrayList<StatusActivity>();
        
        StringBuilder url = new StringBuilder("https://api.twitter.com/1/statuses/user_timeline.json?include_rts=true");
        url.append("&screen_name=").append(this.screenName);
        if (this.lastStatusId != null) {
            url.append("&since_id=").append(this.lastStatusId);
        }
        JsonElement response = JSON.getAsElement(WS.url(url.toString()).get().getString());    // Unauthenticated
        JsonArray array = response.getAsJsonArray();
        DateFormat twitterFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        for (JsonElement element : array) {
            JsonObject tweet = element.getAsJsonObject();
            try {
                final String content = tweet.get("text").getAsString();
                final Date date = twitterFormatter.parse(tweet.get("created_at").getAsString());
                final String statusId = tweet.get("id_str").getAsString();
                final String statusUrl = "http://www.twitter.com/"+this.screenName+"/status/"+statusId;
                statuses.add(new StatusActivity(this.member, date, this.provider, content, statusUrl, statusId));
            } catch (ParseException pe) {
                Logger.error("ouch! parse exception " + pe.getMessage());
            }
        }
        return statuses;
    }

    public void enhance(Collection<StatusActivity> activities) {
        for (StatusActivity activity : activities) {
            Pattern p = Pattern.compile("@([A-Za-z0-9_]+)");
            Matcher m = p.matcher(activity.content);
            while (m.find()) {
                final String mention = m.group(1);
                // By default, we link on Twitter's profile of mentionned user
                String mentionLink = "http://www.twitter.com/"+mention;
                Member mentionedMember = Member.find("twitterName=?", mention).first();
                if (mentionedMember != null) {
                    // If mentionned user is a Link-IT user : we link to its Link-IT profile
                    mentionLink = Router.reverse("Profile.show").add("login", mentionedMember.login).url;
                }
                // Replace original content with enhanced link
                activity.content = activity.content.replace("@"+mention, "<a href=\""+mentionLink+"\">@" + mention + "</a>");
            }
        }
    }
}
