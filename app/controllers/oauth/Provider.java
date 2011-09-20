package controllers.oauth;

import models.Account;
import play.libs.OAuth.ServiceInfo;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public interface Provider {
    
    ServiceInfo getServiceInfo();
    Account getUserAccount(String token, String secret);
}
