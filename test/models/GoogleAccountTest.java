package models;

import java.util.Date;
import models.activity.StatusActivity;
import org.junit.Ignore;
import org.junit.Test;
import play.test.UnitTest;

/**
 * Unit tests for {@link GoogleAccount}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class GoogleAccountTest extends UnitTest {
    
    protected static StatusActivity buildPost(Member author, String content) {
        return new StatusActivity(author, new Date(), ProviderType.Google, content, null, null);
    }
    
    protected static GoogleAccount createAccount(final String gplusId) {
        return new GoogleAccount(gplusId);
    }
    
    @Test
    @Ignore // TODO
    public void enhance() {
    }
    
    @Test
    public void getURL() {
        GoogleAccount ga = createAccount("1234");
        assertEquals("https://profiles.google.com/1234", ga.url());
    }
}
