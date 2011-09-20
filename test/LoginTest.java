import controllers.Login;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import play.test.UnitTest;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class LoginTest extends UnitTest {
    
    @Test
    public void testProviderTwitter() {
        assertNotNull(Login.Providers.TWITTER);
        assertFalse(StringUtils.isBlank(Login.Providers.TWITTER.accessTokenURL));
        assertFalse(StringUtils.isBlank(Login.Providers.TWITTER.authorizationURL));
        assertFalse(StringUtils.isBlank(Login.Providers.TWITTER.requestTokenURL));
        assertFalse(StringUtils.isBlank(Login.Providers.TWITTER.consumerKey));
        assertFalse(StringUtils.isBlank(Login.Providers.TWITTER.consumerSecret));
    }
}
