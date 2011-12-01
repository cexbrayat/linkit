package models.activity;

import helpers.badge.BadgeComputationContext;
import java.util.EnumSet;
import javax.persistence.Entity;
import models.Badge;
import models.Member;
import models.ProviderType;

/**
 * A consultation of something activity : someone ({@link Activity#member}) looked at something on link-it.
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public abstract class LookActivity extends Activity {

    protected LookActivity(Member member) {
        super(ProviderType.LinkIt);
        this.member = member;
    }

    @Override
    protected void computedBadgesForConcernedMembers(BadgeComputationContext context) {
        member.computeBadges(EnumSet.of(Badge.TwoDaysInARow, Badge.FiveDaysInARow, Badge.MixITAddict), context);
    }
}
