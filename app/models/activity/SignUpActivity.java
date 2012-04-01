package models.activity;

import helpers.badge.BadgeComputationContext;
import javax.persistence.Entity;
import models.Member;
import models.ProviderType;
import play.mvc.Router;

/**
 * A sign-up activity : someone ({@link Activity#member} registered on Link-IT
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class SignUpActivity extends Activity {

    public SignUpActivity(Member member) {
        super(ProviderType.LinkIt, 2);
        this.member = member;
        // Useless badge computation
        this.badgeComputationDone = true;
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
        // No badge computation;
    }
}
