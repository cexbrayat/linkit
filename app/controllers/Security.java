package controllers;

import models.auth.LinkItAccount;
import models.*;

/**
 * By default, the login page will accept any login/password.
 * To customize it application has to provide a Security provider which extend Secure.Security class
 * 
 * LinkIt authentication (not OAuth!)
 * 
 * @author agnes007
 */
public class Security extends Secure.Security {

  public static final String ADMIN = "admin";

  public static boolean authenticate(String username, String password) {
        LinkItAccount account = (LinkItAccount) LinkItAccount.find(ProviderType.LinkIt, username);
        return (account != null && account.password.equals(password));
    }
    
        
    public static boolean check(String profile) {
      Member user = Member.findByLogin(connected());
      if(ADMIN.equals(profile))
      {
        return user.hasRole(Role.ADMIN_SESSION) && user.hasRole(Role.ADMIN_MEMBER) &&user.hasRole(Role.ADMIN_PLANNING) &&user.hasRole(Role.ADMIN_SPEAKER);
      }
        return user.hasRole(profile);
    }

    
    static void onDisconnected() {
        Application.index();
    }
}
   