package controllers.oauth;

import models.ProviderType;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import play.test.UnitTest;

/**
 * Unit tests for {@link AbstractOAuthProviderImpl}, and existing concrete implementations.
 * Mainly checks existing configuration
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class AbstractOAuthProviderImplTest extends UnitTest {
    
    protected void assertProvider(OAuthProvider provider) {
        assertNotNull(provider);
        assertFalse(StringUtils.isBlank(provider.getServiceInfo().accessTokenURL));
        assertFalse(StringUtils.isBlank(provider.getServiceInfo().authorizationURL));
        assertFalse(StringUtils.isBlank(provider.getServiceInfo().requestTokenURL));
        assertFalse(StringUtils.isBlank(provider.getServiceInfo().consumerKey));
        assertFalse(StringUtils.isBlank(provider.getServiceInfo().consumerSecret));
        if (provider instanceof AbstractOAuthProviderImpl) {
            AbstractOAuthProviderImpl impl = (AbstractOAuthProviderImpl) provider;
            assertFalse(StringUtils.isBlank(impl.getConfigString("userProfileJsonUrl")));
        } else {
            fail("Not an AbstractOAuthProviderImpl");
        }
    }
    
    @Test
    public void testProviderTwitter() {
        OAuthProvider twitter = OAuthProviderFactory.getProvider(ProviderType.Twitter);
        assertProvider(twitter);
    }
    
    @Test
    public void testProviderGoogle() {
        OAuthProvider google = OAuthProviderFactory.getProvider(ProviderType.Google);
        assertProvider(google);
    }
    
    @Test
    public void testProviderNotOAuth() {
        assertNull(OAuthProviderFactory.getProvider(ProviderType.LinkIt));
    }
}
