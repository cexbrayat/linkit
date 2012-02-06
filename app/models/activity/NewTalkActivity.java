package models.activity;

import helpers.JavaExtensions;
import helpers.badge.BadgeComputationContext;
import java.util.EnumSet;
import javax.persistence.Entity;
import models.Badge;
import models.Member;
import models.ProviderType;
import models.Session;
import play.mvc.Router;

/**
 * An "new talk" activity : a talk ({@link Activity#session}) has been validated
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class NewTalkActivity extends Activity {

    public NewTalkActivity(Session session) {
        super(ProviderType.LinkIt);
        this.session = session;
    }

    @Override
    public String getUrl() {
        return Router
                .reverse("Sessions.show")
                .add("sessionId", session.id)
                .add("slugify", JavaExtensions.slugify(session.title))
                .url;
    }

    @Override
    protected void computedBadgesForConcernedMembers(BadgeComputationContext context) {
        for (Member speaker : session.speakers) {
            speaker.computeBadges(EnumSet.of(Badge.Speaker), context);
        }
    }
}
