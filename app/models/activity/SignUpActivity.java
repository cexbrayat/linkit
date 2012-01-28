package models.activity;

import helpers.badge.BadgeComputationContext;
import javax.persistence.Entity;
import models.Member;
import models.ProviderType;
import play.i18n.Messages;
import play.mvc.Router;
import play.mvc.Scope;

/**
 * A sign-up activity : someone ({@link Activity#member} registered on Link-IT
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class SignUpActivity extends Activity {

    public SignUpActivity(Member member) {
        super(ProviderType.LinkIt);
        this.member = member;
        // Useless badge computation
        this.badgeComputationDone = true;
    }

    @Override
    public String getMessage(Scope.Session s) {
        return Messages.get(getMessageKey(), renderMention(member, s));
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
