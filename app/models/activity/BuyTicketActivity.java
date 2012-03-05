package models.activity;

import helpers.badge.BadgeComputationContext;
import java.util.EnumSet;
import javax.persistence.Entity;
import models.Badge;
import models.Member;
import models.ProviderType;
import play.mvc.Router;

/**
 * A "buy ticket" activity : someone ({@link Activity#member} bought its ticket.
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class BuyTicketActivity extends Activity {

    public BuyTicketActivity(Member author) {
        super(ProviderType.LinkIt, 3);
        this.member = author;
    }

    @Override
    protected void computedBadgesForConcernedMembers(BadgeComputationContext context) {
        member.computeBadges(EnumSet.of(Badge.Attendee), context);
    }

    @Override
    public String getUrl() {
        return Router
            .reverse("Profile.show")
            .add("login", member.login)
            .url;
    }
}
