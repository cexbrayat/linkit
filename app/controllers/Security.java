package controllers;

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
    
    public static boolean authenticate(String username, String password) {
        LinkItAccount account = (LinkItAccount) Account.find(ProviderType.LinkIt, username);
        return (account != null && account.password.equals(password));
    }
    
        
    public static boolean check(String profile) {
        Member user = Member.findByLogin(connected());
        return user.hasRole(profile);
    }

    
    static void onDisconnected() {
        Application.index();
    }
    
    static void onAuthenticated() {
        Profile.show(connected());
    }
}
   