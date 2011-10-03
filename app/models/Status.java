package models;

import play.Logger;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import play.libs.WS;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

public class Status implements Comparable<Status> {

    public String content;

    public Date date;

	static DateFormat twitterFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy",Locale.ENGLISH);

	public Status(String content, Date date){
		this.content = content;
		this.date = date;
	}

    public static List<Status> getStatuses(Account account) {
		List<Status> statuses = new ArrayList<Status>();
		if(account != null){
			switch(account.provider){
			case Google : 
				JsonElement gResponse = WS.url("https://www.googleapis.com/plus/v1/people/" + ((GoogleAccount)account).googleId + "/activities/public?key=" + ((GoogleAccount)account).token).get().getJson();
				Logger.info("url {" + "https://www.googleapis.com/plus/v1/people/" + ((GoogleAccount)account).googleId + "/activities/public?key=" + ((GoogleAccount)account).token + "}");
				statuses.add(new Status("hello g+", new Date()));
				break;
			case Twitter : 
				JsonElement tResponse = WS.url("https://api.twitter.com/1/statuses/user_timeline.json?include_entities=true&include_rts=true&screen_name=" + ((TwitterAccount)account).screenName + "&count=10").get().getJson();
				JsonArray array = tResponse.getAsJsonArray();
				for(JsonElement element : array){ 
					JsonObject tweet = element.getAsJsonObject();
					try{
						statuses.add(new Status(tweet.get("text").getAsString(), twitterFormatter.parse(tweet.get("created_at").getAsString())));
					}catch(ParseException pe){
						Logger.error("ouch! parse exception");
					}
				}
				break;
			}
		}
		return statuses;
    }

    @Override
    public String toString() {
        return content;
    }

	@Override
	public int compareTo(Status other){
		return (this.date.compareTo(other.date));
	}
}
