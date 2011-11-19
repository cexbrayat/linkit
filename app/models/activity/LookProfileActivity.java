package models.activity;

import helpers.badge.BadgeComputationContext;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import models.Member;
import models.ProviderType;
import play.i18n.Messages;
import play.mvc.Router;

/**
 * An consultation profile activity : someone ({@link Activity#member} looked at someone else's ({@link LookProfileActivity#other} profile.
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class LookProfileActivity extends Activity {

    @ManyToOne
    public Member other;

    public LookProfileActivity(Member member, Member consulted) {
        super(ProviderType.LinkIt);
        this.member = member;
        this.other = consulted;
    }

    @Override
    public String getMessage(String lang) {
        return Messages.get(getMessageKey(), member, other);
    }

    @Override
    public String getUrl() {
        return Router
                .reverse("Profile.show")
                .add("login", other.login)
                .url;
    }

    @Override
    protected void computedBadgesForConcernedMembers(BadgeComputationContext context) {
        // No badge computation;
    }
}
