package helpers.oauth;

import java.util.HashMap;
import java.util.Map;
import models.ProviderType;

/**
 * Factory of {@link OAuthProvider} implementations
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class OAuthProviderFactory {

    private static final Map<ProviderType, OAuthProvider> providers = new HashMap<ProviderType, OAuthProvider>();
    static {
        providers.put(ProviderType.Twitter, new Twitter());
        providers.put(ProviderType.Google, new Google());
        providers.put(ProviderType.LinkedIn, new LinkedIn());
    }

    public static OAuthProvider getProvider(ProviderType type) {
        return providers.get(type);
    }
}
