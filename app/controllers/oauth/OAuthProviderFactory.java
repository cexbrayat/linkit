package controllers.oauth;

import java.util.HashMap;
import java.util.Map;
import models.ProviderType;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class OAuthProviderFactory {

    private static final Map<ProviderType, OAuthProvider> providers = new HashMap<ProviderType, OAuthProvider>();
    static {
        providers.put(ProviderType.Twitter, new Twitter());
    }

    public static OAuthProvider getProvider(ProviderType type) {
        return providers.get(type);
    }
}
