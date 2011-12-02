package models.activity;

import helpers.badge.BadgeComputationContext;
import java.util.EnumSet;
import javax.persistence.Entity;
import models.Badge;
import models.Member;
import models.ProviderType;

/**
 * A comment activity : someone ({@link Activity#member} commented on something
 * @author Agnes <agnes.crepet@gmail.com>
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public abstract class CommentActivity extends Activity {

    public CommentActivity(Member author) {
        super(ProviderType.LinkIt);
        this.member = author;
    }

    @Override
    protected void computedBadgesForConcernedMembers(BadgeComputationContext context) {
        member.computeBadges(EnumSet.of(Badge.Brave, Badge.Troller), context);
    }
}
