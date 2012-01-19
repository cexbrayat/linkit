package models.activity;

import helpers.badge.BadgeComputationContext;
import java.util.EnumSet;
import javax.persistence.Entity;
import models.Badge;
import models.LightningTalk;
import models.Member;
import models.ProviderType;
import models.Session;
import models.Talk;
import play.i18n.Messages;
import play.mvc.Scope;

/**
 * An "update session" activity : a publicly visible session ({@link Activity#session}), i.e any {@link LightningTalk} or a valid {@link Talk}, has been updated.
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class UpdateSessionActivity extends Activity {

    public UpdateSessionActivity(Session session) {
        super(ProviderType.LinkIt);
        this.session = session;
    }

    @Override
    public String getMessage(Scope.Session session) {
        return Messages.get(getMessageKey(), session);
    }

    @Override
    public String getUrl() {
        return this.session.getShowUrl();
    }

    @Override
    protected void computedBadgesForConcernedMembers(BadgeComputationContext context) {
        for (Member speaker : session.speakers) {
            speaker.computeBadges(EnumSet.of(Badge.Speaker, Badge.SpeakerFan), context);
        }
    }
}
