package models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import play.Logger;
import play.libs.WS;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Status implements Comparable<Status> {

    public String content;

    public Date date;

    public ProviderType provider;

    //Wed Oct 05 12:42:55 +0000 2011
    static DateFormat twitterFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);

    //2011-10-04T14:41:40.837Z
    static DateFormat googleFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

    public Status(String content, Date date, ProviderType provider) {
        this.content = content;
        this.date = date;
        this.provider = provider;
    }

    public static List<Status> getStatuses(ProviderType type, String account) {
        List<Status> statuses = new ArrayList<Status>();
        Logger.info("account {" + account + "}");
        if (account != null) {
            switch (type) {
                case Google:
                    JsonElement gResponse = WS.url("https://www.googleapis.com/plus/v1/people/" + account + "/activities/public?key=AIzaSyC4xOkQsEPJcUKUvQGL6T7RZkrIIxSuZAg").get().getJson();
                    try {
                        JsonObject feed = gResponse.getAsJsonObject();
                        JsonArray activities = feed.get("items").getAsJsonArray();
                        for (JsonElement element : activities) {
                            JsonObject activity = element.getAsJsonObject();
                            String content = activity.get("object").getAsJsonObject().get("content").getAsString();
                            content = content.length() > 150 ? content.substring(0, 150) + "..." : content;
                            Date date = googleFormatter.parse(activity.get("published").getAsString());
                            statuses.add(new Status(content, date, ProviderType.Google));
                        }
                    } catch (ParseException pe) {
                        Logger.error("ouch! parse exception " + pe.getMessage());
                    }
                    break;
                case Twitter:
                    JsonElement tResponse = WS.url("https://api.twitter.com/1/statuses/user_timeline.json?include_entities=true&include_rts=true&screen_name=" + account + "&count=10").get().getJson();
                    JsonArray array = tResponse.getAsJsonArray();
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
                            statuses.add(new Status(content, date, ProviderType.Twitter));
                        } catch (ParseException pe) {
                            Logger.error("ouch! parse exception " + pe.getMessage());
                        }
                    }
                    break;
            }
        }
        Logger.info("statuses {" + statuses + "}");
        return statuses;
    }

    @Override
    public String toString() {
        return content + " at " + date.toString();
    }

    public int compareTo(Status other) {
        return (other.date.compareTo(this.date));
    }
}
