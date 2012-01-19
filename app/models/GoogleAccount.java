package models;

import java.util.regex.Matcher;
import jodd.lagarto.dom.jerry.Jerry;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpRequest;
import com.google.api.client.http.json.JsonHttpRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusRequest;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.api.services.plus.model.ActivityObjectActor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import javax.persistence.Entity;
import jodd.lagarto.dom.jerry.JerryFunction;
import models.activity.StatusActivity;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.Play;
import play.data.validation.Required;
import static jodd.lagarto.dom.jerry.Jerry.jerry;

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
    
    static final Pattern PROFILE_URL = Pattern.compile("^https://plus.google.com/(\\d+)/?$");

    static class PlusRequestInitializer implements JsonHttpRequestInitializer {

        public void initialize(JsonHttpRequest request) {
            PlusRequest plusRequest = (PlusRequest) request;
            plusRequest.setPrettyPrint(true);
            plusRequest.setKey(Play.configuration.getProperty("Google.apiKey"));
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
    
    public static Member findMemberByGoogleId(final String googleId) {
        return find("select ga.member from GoogleAccount ga where ga.googleId=?", googleId).first();
    }

    public List<StatusActivity> fetchActivities() {
        List<StatusActivity> statuses = new ArrayList<StatusActivity>();

        DateFormat googleFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        try {
            ActivityFeed feed = api.activities().list(this.googleId, "public").execute();
            for (Activity activity : feed.getItems()) {
                String content = activity.getPlusObject().getContent();
                if ("share".equals(activity.getVerb())) {
                    content = "<br/>Message original de " + mention(activity.getPlusObject().getActor()) + " : <div class='google reshare'>" + content + "</div>";
                }
                String annotation = activity.getAnnotation();
                if (StringUtils.isNotBlank(annotation)) {
                    content = annotation + content;
                }
                Date date = googleFormatter.parse(activity.getPublished().toStringRfc3339());
                statuses.add(new StatusActivity(this.member, date, this.provider, content, activity.getUrl(), activity.getId()));
            }
        } catch (Exception e) {
            Logger.error(e, "Exception while fetching Google feed for %s : %s", this.member, e.getMessage());
        }
        return statuses;
    }

    static private String mention(ActivityObjectActor actor) {
        return mention(actor.getId(), actor.getDisplayName());
    }

    static private String mention(String googleId, String defaultMention) {
        String mention = defaultMention;
        Member mentionedMember = findMemberByGoogleId(googleId);
        if (mentionedMember != null) {
            mention = StatusActivity.buildMentionFor(mentionedMember);
        }
        return mention;
    }

    public void enhance(Collection<StatusActivity> activities) {
        for (StatusActivity activity : activities) {
            activity.content = replaceMentions(activity.content);
        }
    }
    
    protected static String replaceMentions(String content) {
        Jerry doc = jerry(content);
        doc.$("a").each(new JerryFunction() {

            public boolean onNode(Jerry linkHtml, int i) {
                final String link = linkHtml.attr("href");
                Matcher matcher = PROFILE_URL.matcher(link);
                if (matcher.matches()) {
                    final String mentionedId = matcher.group(1);
                    final Jerry mentionHtml = linkHtml.parent();
                    mentionHtml.html(mention(mentionedId, mentionHtml.html()));
                }
                return true;
            }
        });
        return doc.html();
    }

    @Override
    public String url() {
        return new StringBuilder("https://profiles.google.com/").append(googleId).toString();
    }
}
