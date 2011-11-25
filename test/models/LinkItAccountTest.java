package models;

import org.junit.Test;
import play.test.UnitTest;

/**
 * Unit tests for {@link LinkItAccount}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class LinkItAccountTest extends UnitTest {
    
    protected static LinkItAccount createMemberAndAccount(final String login, final String password) {
        LinkItAccount a = new LinkItAccount(password);
        new Member(login, a).save();
        return a;
    }
    
    @Test
    public void getURL() {
        LinkItAccount a = createMemberAndAccount("toto", "password");
        assertNotNull(a.url());
    }
}
