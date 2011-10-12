package models.activity;

import javax.persistence.Entity;
import models.Member;
import models.ProviderType;
import play.i18n.Messages;
import play.mvc.Router;

/**
 * A sign-up activity : someone ({@link Activity#member} registered on Link-IT
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class SignUpActivity extends Activity {

    public SignUpActivity(Member member) {
        super();
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

    @Override
    public ProviderType getProvider() {
        return ProviderType.LinkIt;
    }
}
