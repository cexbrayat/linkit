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
import javax.persistence.Entity;
import models.activity.StatusActivity;
import play.Logger;
import play.data.validation.Required;
import play.libs.WS;

/**
 * A Google account
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class GoogleAccount extends Account {
    
    /** Google+ ID, i.e https://plus.google.com/{ThisFuckingLongNumber} as seen on Google+' profile link */
    @Required
    public String googleId;     // 114128610730314333831

    //2011-10-04T14:41:40.837Z
    static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    
    public GoogleAccount(final String googleId) {
        super(ProviderType.Google);
        this.googleId = googleId;
    }
    
    @Override
    public String toString(){
        return "Google+ account " + googleId;
    }

    public static GoogleAccount findByEmail(final String email) {
        return GoogleAccount.find("member.email = ?", email).first();
    }

    public List<StatusActivity> fetchActivities() {
        List<StatusActivity> statuses = new ArrayList<StatusActivity>();
        
        StringBuilder url = new StringBuilder("https://www.googleapis.com/plus/v1/people/")
                .append(this.googleId)
                .append("/activities/public?key=AIzaSyC4xOkQsEPJcUKUvQGL6T7RZkrIIxSuZAg");
        JsonObject response = JSON.getAsObject(WS.url(url.toString()).get().getString());    // Unauthenticated
        JsonArray activities = response.get("items").getAsJsonArray();
        DateFormat googleFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        for (JsonElement element : activities) {
            JsonObject activity = element.getAsJsonObject();
            try {
                String content = activity.get("object").getAsJsonObject().get("content").getAsString();
                Date date = googleFormatter.parse(activity.get("published").getAsString());
                String statusId = activity.get("id").getAsString();
                String statusUrl = activity.get("url").getAsString();
                statuses.add(new StatusActivity(this.member, date, this.provider, content, statusUrl, statusId));
            } catch (ParseException pe) {
                Logger.error("ouch! parse exception " + pe.getMessage());
            }
        }
        return statuses;
     }

    public void enhance(Collection<StatusActivity> activities) {
        // TODO Google enhance
    }

    @Override
    public String getUrl() {
        return new StringBuilder("https://profiles.google.com/").append(googleId).toString();
    }
}
