package models.activity;

import helpers.badge.BadgeComputationContext;
import java.util.EnumSet;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import models.Badge;
import models.Member;
import models.ProviderType;
import play.data.validation.Required;
import play.mvc.Router;

/**
 * A link activity : someone ({@link Activity#member} starts to follow someone else ({@link LinkActivity#other}
 * @author Agnes <agnes.crepet@gmail.com>
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class LinkActivity extends Activity {

    @Required
    @ManyToOne
    public Member other;

    public LinkActivity(Member member, Member linked) {
        super(ProviderType.LinkIt, 3);
        this.member = member;
        this.other = linked;
    }

    @Override
    public String getUrl() {
        return Router
                .reverse("Profile.show")
                .add("login", member.login)
                .addRef("linkTo"+other.login)
                .url;
    }

    @Override
    protected void computedBadgesForConcernedMembers(BadgeComputationContext context) {
        // Linker
        member.computeBadges(EnumSet.of(Badge.StaffBestFriend, Badge.SpeakerFan, Badge.SponsorFriendly, Badge.NewBorn, Badge.Friendly, Badge.SocialBeast, Badge.MadLinker, Badge.TwoDaysInARow, Badge.FiveDaysInARow, Badge.MixITAddict), context);
        // Linked
        other.computeBadges(EnumSet.of(Badge.YouReNotAlone, Badge.LocalCelebrity, Badge.RockStar, Badge.Leader, Badge.Idol), context);
    }
}
