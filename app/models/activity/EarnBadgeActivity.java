package models.activity;

import java.util.EnumSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import models.Badge;
import models.Member;
import models.ProviderType;
import play.i18n.Messages;
import play.mvc.Router;

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
    }

    @Override
    public String getMessage(String lang) {
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
    public Set<Badge> getPotentialTriggeredBadges() {
        return EnumSet.noneOf(Badge.class);
    }
}
