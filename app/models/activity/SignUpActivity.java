package models.activity;

import javax.persistence.Entity;
import models.Member;
import play.i18n.Messages;

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
}
