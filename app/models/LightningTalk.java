package models;

import helpers.JavaExtensions;
import play.Logger;

import javax.persistence.*;
import java.util.List;
import play.modules.search.Indexed;
import play.mvc.Router;

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
