package models.activity;

import helpers.badge.BadgeComputationContext;
import java.util.EnumSet;
import javax.persistence.Entity;
import models.Badge;
import models.LightningTalk;
import models.Member;
import models.ProviderType;

/**
 * An "new vote" activity : someone {@link Activity#member} has voted for a lightning talk ({@link Activity#session})
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class NewVoteActivity extends Activity {

    public NewVoteActivity(Member member, LightningTalk lt) {
        super(ProviderType.LinkIt);
        this.member = member;
        this.session = lt;
    }

    @Override
    public String getUrl() {
        return session.getShowUrl();
    }

    @Override
    protected void computedBadgesForConcernedMembers(BadgeComputationContext context) {
        member.computeBadges(EnumSet.of(Badge.Supporter, Badge.Enlightened), context);
    }
}
