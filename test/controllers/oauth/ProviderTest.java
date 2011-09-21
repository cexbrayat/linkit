package controllers.oauth;

import models.ProviderType;
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
        OAuthProvider twitter = OAuthProviderFactory.getProvider(ProviderType.Twitter);
        assertNotNull(twitter);
        assertFalse(StringUtils.isBlank(twitter.getServiceInfo().accessTokenURL));
        assertFalse(StringUtils.isBlank(twitter.getServiceInfo().authorizationURL));
        assertFalse(StringUtils.isBlank(twitter.getServiceInfo().requestTokenURL));
        assertFalse(StringUtils.isBlank(twitter.getServiceInfo().consumerKey));
        assertFalse(StringUtils.isBlank(twitter.getServiceInfo().consumerSecret));
    }
    
    @Test
    public void testProviderNotOAuth() {
        assertNull(OAuthProviderFactory.getProvider(ProviderType.LinkIt));
    }
}
