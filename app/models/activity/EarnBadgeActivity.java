package models.activity;

import helpers.badge.BadgeComputationContext;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import models.Badge;
import models.Member;
import models.ProviderType;
import play.i18n.Messages;
import play.mvc.Router;
import play.mvc.Scope;

/**
 * A "badge earned" activity : someone ({@link Activity#member}) earned a badge ({@link EarnBadgeActivity#badge})
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class EarnBadgeActivity extends Activity {
    
    @Enumerated(EnumType.STRING)
    public Badge badge;

    public EarnBadgeActivity(Member member, Badge badge) {
        super(ProviderType.LinkIt);
        this.member = member;
        this.badge = badge;
        // Useless badge computation
        this.badgeComputationDone = true;
    }

    @Override
    public String getMessage(Scope.Session s) {
        return Messages.get(getMessageKey(), member, badge);
    }

    @Override
    public String getUrl() {
        return Router
                .reverse("Profile.show")
                .add("login", member.login)
                .addRef("badge"+badge)
                .url;
    }

    @Override
    protected void computedBadgesForConcernedMembers(BadgeComputationContext context) {
        // No badge computation;
    }
}
