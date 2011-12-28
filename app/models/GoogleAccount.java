package models;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpRequest;
import com.google.api.client.http.json.JsonHttpRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusRequest;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import java.text.DateFormat;
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

    static class PlusRequestInitializer implements JsonHttpRequestInitializer {

        public void initialize(JsonHttpRequest request) {
            PlusRequest plusRequest = (PlusRequest) request;
            plusRequest.setPrettyPrint(true);
            plusRequest.setKey("AIzaSyC4xOkQsEPJcUKUvQGL6T7RZkrIIxSuZAg");
        }
    }
    static final Plus api = Plus.builder(new NetHttpTransport(), new GsonFactory()).setJsonHttpRequestInitializer(new PlusRequestInitializer()).build();

    public GoogleAccount(final String googleId) {
        super(ProviderType.Google);
        this.googleId = googleId;
    }

    @Override
    public String toString() {
        return "Google+ account " + googleId;
    }

    public static GoogleAccount findByEmail(final String email) {
        return GoogleAccount.find("member.email = ?", email).first();
    }

    public List<StatusActivity> fetchActivities() {
        List<StatusActivity> statuses = new ArrayList<StatusActivity>();

        DateFormat googleFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        try {
            ActivityFeed feed = api.activities().list(this.googleId, "public").execute();
            for (Activity activity : feed.getItems()) {
                String content = activity.getPlusObject().getContent();
                Date date = googleFormatter.parse(activity.getPublished().toStringRfc3339());
                statuses.add(new StatusActivity(this.member, date, this.provider, content, activity.getUrl(), activity.getId()));
            }
        } catch (Exception e) {
            Logger.error(e, "Exception while fetching Google feed for %s : %s", this.member, e.getMessage());
        }
        return statuses;
    }

    public void enhance(Collection<StatusActivity> activities) {
        // TODO Google enhance
    }

    @Override
    public String url() {
        return new StringBuilder("https://profiles.google.com/").append(googleId).toString();
    }
}
