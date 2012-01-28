package controllers;

import models.Member;
import models.ProviderType;
import models.Role;
import models.auth.LinkItAccount;
import play.Play;
import play.libs.Crypto;
import play.libs.Crypto.HashType;

/**
 * By default, the login page will accept any login/password. To customize it
 * application has to provide a Security provider which extend Secure.Security
 * class
 * 
 * LinkIt authentication (not OAuth!)
 * 
 * @author agnes007
 */
public class Security extends Secure.Security {

	public static final String ADMIN = "admin";
	public static final String MEMBER = "member";

	public static boolean authenticate(String username, String password) {
		LinkItAccount account = (LinkItAccount) LinkItAccount.find(
				ProviderType.LinkIt, username);

		// Retrieve salt from configuration
		String salt = Play.configuration.get("application.salt").toString();
		String passwordHash = Crypto.passwordHash(password + salt, HashType.SHA256);

		return (account != null && account.password.equals(passwordHash));
	}

	public static boolean check(String profile) {
		if (isConnected()) {
			Member user = Member.findByLogin(connected());
			if (ADMIN.equals(profile)) {
				return user.hasRole(Role.ADMIN_SESSION)
						&& user.hasRole(Role.ADMIN_MEMBER)
						&& user.hasRole(Role.ADMIN_PLANNING)
						&& user.hasRole(Role.ADMIN_SPEAKER);
			}
            else if (MEMBER.equals(profile))
            {
                // TODO JRI HOW TO do that, no role ...
                return true;
            }
			return user.hasRole(profile);
		}
		return false;
	}

	static void onDisconnected() {
		Application.index();
	}
}
