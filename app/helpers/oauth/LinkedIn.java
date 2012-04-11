package helpers.oauth;

import helpers.XML;
import models.auth.OAuthAccount;
import models.ProviderType;
import models.auth.LinkedInOAuthAccount;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.oauth.OAuthService;
import org.w3c.dom.Document;

/**
 * Twitter OAuth provider
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class LinkedIn extends AbstractOAuthProviderImpl {

    public LinkedIn() {
        super(ProviderType.LinkedIn);
    }

    @Override
    protected OAuthService buildService() {
         return new ServiceBuilder()
            .provider(LinkedInApi.class)
            .apiKey(getConfigString("consumerKey"))
            .apiSecret(getConfigString("consumerSecret"))
            .callback(getCallbackUrl())
            .build();
    }

    public OAuthAccount getUserAccount(String token, String secret) {

        final String url = getConfigString("userProfileJsonUrl");
        final Document dom = XML.getDocument(get(url, token, secret));

        LinkedInOAuthAccount account = new LinkedInOAuthAccount(token, secret);
        account.linkedInId = XML.getFirstNodeValue(dom, "id");
        account.firstname = XML.getFirstNodeValue(dom, "first-name");
        account.lastname = XML.getFirstNodeValue(dom, "last-name");
        account.headline = XML.getFirstNodeValue(dom, "headline");
        account.summary = XML.getFirstNodeValue(dom, "summary");

        return account;
    }

    @Override
    public OAuthAccount getEmptyUserAccount(String oauth_login) {
        //TODO JRI
        return null;
    }
}
