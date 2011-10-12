package models.activity;

import javax.persistence.Entity;
import models.Member;
import models.ProviderType;
import play.i18n.Messages;
import play.mvc.Router;

/**
 * An update profile activity : someone ({@link Activity#member} updated his profile
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class UpdateProfileActivity extends Activity {

    public UpdateProfileActivity(Member member) {
        super(ProviderType.LinkIt);
        this.member = member;
    }

    @Override
    public String getMessage(String lang) {
        return Messages.get(getMessageKey(), member);
    }

    @Override
    public String getUrl() {
        return Router
                .reverse("Profile.show")
                .add("login", member.login)
                .toString();
    }
}
