package models.activity;

import java.util.EnumSet;
import java.util.Set;
import javax.persistence.Entity;
import models.Badge;
import models.ProviderType;
import models.Session;
import play.i18n.Messages;
import play.mvc.Router;

/**
 * An "update session" activity : a session ({@link Activity#session}) has been updated
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class UpdateSessionActivity extends Activity {

    public UpdateSessionActivity(Session session) {
        super(ProviderType.LinkIt);
        this.session = session;
    }

    @Override
    public String getMessage(String lang) {
        return Messages.get(getMessageKey(), session);
    }

    @Override
    public String getUrl() {
        return Router
                .reverse("Sessions.show")
                .add("sessionId", session.id)
                .url;
    }

    @Override
    public Set<Badge> getPotentialTriggeredBadges() {
        return EnumSet.noneOf(Badge.class);
    }
}
