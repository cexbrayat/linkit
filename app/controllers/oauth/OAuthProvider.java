package controllers.oauth;

import models.OAuthAccount;
import play.libs.OAuth.ServiceInfo;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public interface OAuthProvider {
    
    ServiceInfo getServiceInfo();
    OAuthAccount getUserAccount(String token, String secret);
}
