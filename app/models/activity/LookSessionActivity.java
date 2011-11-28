package models.activity;

import helpers.badge.BadgeComputationContext;
import java.util.EnumSet;
import javax.persistence.Entity;
import models.Badge;
import models.Member;
import models.ProviderType;
import models.Session;
import play.i18n.Messages;
import play.mvc.Router;

/**
 * A "looked at session" activity : someone ({@link Activity#member}) looked at a session ({@link Activity#session}).
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class LookSessionActivity extends Activity {

    public LookSessionActivity(Member member, Session session) {
        super(ProviderType.LinkIt);
        this.member = member;
        this.session = session;
    }

    @Override
    public String getMessage(String lang) {
        return Messages.get(getMessageKey(), member, session);
    }

    @Override
    public String getUrl() {
        return Router
                .reverse("Sessions.show")
                .add("sessionId", session.id)
                .url;
    }

    @Override
    protected void computedBadgesForConcernedMembers(BadgeComputationContext context) {
        member.computeBadges(EnumSet.of(Badge.TwoDaysInARow, Badge.FiveDaysInARow, Badge.MixITAddict), context);
    }
}
