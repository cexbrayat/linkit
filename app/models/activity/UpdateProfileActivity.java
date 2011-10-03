package models.activity;

import javax.persistence.Entity;
import models.Member;
import play.i18n.Messages;

/**
 * A update profile activity : someone ({@link Activity#member} updated his profile
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class UpdateProfileActivity extends Activity {

    public UpdateProfileActivity(Member member) {
        super();
        this.member = member;
    }

    @Override
    public String getMessage(String lang) {
        return Messages.get(getMessageKey(), member);
    }
}
