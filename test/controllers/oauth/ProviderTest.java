package controllers.oauth;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import play.test.UnitTest;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class ProviderTest extends UnitTest {
    
    @Test
    public void testProviderTwitter() {
        Provider twitter = ProviderFactory.getProvider("twitter");
        assertNotNull(twitter);
        assertFalse(StringUtils.isBlank(twitter.getServiceInfo().accessTokenURL));
        assertFalse(StringUtils.isBlank(twitter.getServiceInfo().authorizationURL));
        assertFalse(StringUtils.isBlank(twitter.getServiceInfo().requestTokenURL));
        assertFalse(StringUtils.isBlank(twitter.getServiceInfo().consumerKey));
        assertFalse(StringUtils.isBlank(twitter.getServiceInfo().consumerSecret));
    }
    
    @Test
    public void testProviderUnknown() {
        assertNull(ProviderFactory.getProvider("abcd"));
    }
}
