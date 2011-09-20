package controllers.oauth;

import play.Play;
import play.libs.OAuth.ServiceInfo;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public abstract class AbstractProvider implements OAuthProvider {

    static protected ServiceInfo getServiceInfo(String provider) {
        final String requestTokenURL = Play.configuration.getProperty(provider+".requestTokenUrl");
        final String accessTokenURL = Play.configuration.getProperty(provider+".accessTokenUrl");
        final String authorizeURL = Play.configuration.getProperty(provider+".authorizeUrl");
        final String consumerKey = Play.configuration.getProperty(provider+".consumerKey");
        final String consumerSecret = Play.configuration.getProperty(provider+".consumerSecret");
        return new ServiceInfo(requestTokenURL, accessTokenURL, authorizeURL, consumerKey, consumerSecret);
    }
}
