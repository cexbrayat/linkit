package models;

import helpers.JavaExtensions;
import play.modules.search.Indexed;
import play.mvc.Router;

import javax.persistence.Entity;
import java.util.List;

/**
 * @author Julien Ripault <tluapir@gmail.com>
 */
@Entity
@Indexed
public class LightningTalk extends Session {

    public LightningTalk() {
        super();
        // A Lightning Talk is always validated
        valid = true;
    }

    public static List<LightningTalk> findAllOn(ConferenceEvent event) {
        return find("event = ?", event).fetch();
    }

    public static List<Member> findAllSpeakersOn(ConferenceEvent event) {
        return find("select distinct s from LightningTalk t inner join t.speakers s where t.event = ? order by s.lastname", event).fetch();
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public String getShowUrl() {
        return Router
                .reverse("LightningTalks.show")
                .add("sessionId", this.id)
                .add("slugify", JavaExtensions.slugify(this.title))
                .url;
    }

    public static List<LightningTalk> findLinkedWith(Interest interest) {
        return find("? in elements(interests)", interest).fetch();
   }
}
