package models.activity;

import models.BaseDataUnitTest;
import models.Member;
import org.apache.commons.lang.StringUtils;
import play.mvc.Scope;

/**
 * Abstract class for units tests of {@link Activity} and derived classes.
 * @author Sryl <cyril.lacote@gmail.com>
 */
public abstract class AbstractActivityTest extends BaseDataUnitTest {

    public static final Scope.Session DEFAULT_SESSION = null;
        
    /** One alone member (no links, no linker) */
    protected Member member;

    protected Member createMember(final String login) {
        return new Member(login).save();
    }

    @Override
    public void setUp() {
        super.setUp();
        member = createMember("toto");
    }

    protected void assertActivity(final Activity activity) {
        assertNotNull(activity);
        assertNotNull(activity.at);
        assertTrue(StringUtils.isNotBlank(activity.getMessage(DEFAULT_SESSION)));
        assertFalse(activity.getMessage(DEFAULT_SESSION).equals(activity.getMessageKey()));
    }
}
