package models.auth;

import javax.persistence.Entity;
import javax.persistence.Lob;
import models.Member;
import models.ProviderType;
import org.apache.commons.lang.StringUtils;
import play.templates.JavaExtensions;

/**
 * A LinkedIn account
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class LinkedInOAuthAccount extends OAuthAccount {

    public String linkedInId;
    public String firstname;
    public String lastname;
    public String headline;

    @Lob
    public String summary;
    
    public LinkedInOAuthAccount(String token, String secret) {
        super(ProviderType.LinkedIn, token, secret);
    }

    @Override
    public String getOAuthLogin() {
        return JavaExtensions.slugify(firstname, true)+'.'+JavaExtensions.slugify(lastname, true);
        // return linkedInId;
    }

    @Override
    public void initMemberProfile() {
        if (member != null) {
            if (StringUtils.isBlank(member.login)) member.login = getOAuthLogin();
            if (StringUtils.isBlank(member.firstname)) member.firstname = this.firstname;
            if (StringUtils.isBlank(member.lastname)) member.lastname = this.lastname;
            if (StringUtils.isBlank(member.shortDescription)) member.shortDescription = this.headline;
            if (StringUtils.isBlank(member.longDescription)) member.longDescription = this.summary;
        }
    }

    @Override
    public Member findCorrespondingMember() {
        return Member.findByLogin(getOAuthLogin());
    }
}
