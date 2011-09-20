package controllers.oauth;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class ProviderFactory {

    private static final Map<String, Provider> providers = new HashMap<String, Provider>();
    static {
        providers.put("twitter", new Twitter()); 
    }
    
    public static Provider getProvider(String providerId) {
        return providers.get(providerId);
    }
}
