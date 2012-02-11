package models.activity;

import helpers.badge.BadgeComputationContext;
import java.util.EnumSet;
import javax.persistence.Entity;
import models.Badge;
import models.Member;
import models.ProviderType;
import play.mvc.Router;

/**
 * An update profile activity : someone ({@link Activity#member} updated his profile
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class UpdateProfileActivity extends Activity {

    public UpdateProfileActivity(Member member) {
        super(ProviderType.LinkIt, 3);
        this.member = member;
    }

    @Override
    public String getUrl() {
        return Router
                .reverse("Profile.show")
                .add("login", member.login)
                .url;
    }

    @Override
    protected void computedBadgesForConcernedMembers(BadgeComputationContext context) {
        member.computeBadges(EnumSet.of(Badge.TwoDaysInARow, Badge.FiveDaysInARow, Badge.MixITAddict), context);
    }
}
