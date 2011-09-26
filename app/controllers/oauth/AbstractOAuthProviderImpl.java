package controllers.oauth;

import com.google.gson.JsonObject;
import java.util.concurrent.ExecutionException;
import models.ProviderType;
import play.Logger;
import play.Play;
import play.libs.F.Promise;
import play.libs.OAuth.ServiceInfo;
import play.libs.WS;
import play.libs.WS.HttpResponse;

/**
 * Default implementation of {@link OAuthProvider}
 * @author Sryl <cyril.lacote@gmail.com>
 */
abstract class AbstractOAuthProviderImpl implements OAuthProvider {

    private ProviderType provider;

    private ServiceInfo serviceInfo;

    AbstractOAuthProviderImpl(ProviderType provider) {
        this.provider = provider;
        this.serviceInfo = buildServiceInfo();
    }

    public ServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    final public ServiceInfo buildServiceInfo() {
        final String requestTokenURL = getConfigString("requestTokenUrl");
        final String accessTokenURL = getConfigString("accessTokenUrl");
        final String authorizeURL = getConfigString("authorizeUrl");
        final String consumerKey = getConfigString("consumerKey");
        final String consumerSecret = getConfigString("consumerSecret");
        return new ServiceInfo(requestTokenURL, accessTokenURL, authorizeURL, consumerKey, consumerSecret);
    }

    /**
     * Get configuration String for current provider
     * @param key Key to retrieve for current provider
     * @return Property value found
     */
    String getConfigString(String key) {
        final String providerKey = new StringBuilder(provider.name()).append('.').append(key).toString();
        return Play.configuration.getProperty(providerKey);
    }

    public HttpResponse get(String url, String token, String secret) {
        Promise<HttpResponse> response = WS.url(url)
                .oauth(getServiceInfo(), token, secret)
                .getAsync();
        try {
            return response.get();
        } catch (InterruptedException ex) {
            Logger.error(ex, "OAuthenticated HTTP GET interrupted");
        } catch (ExecutionException ex) {
            Logger.error(ex, "OAuthenticated HTTP GET interrupted");
        }
        return null;
    }

    static String getStringPropertyFromJson(JsonObject object, String property) {
        if (object.get(property) != null) {
            return object.get(property).getAsString();
        } else {
            return null;
        }
    }
    
    static Long getLongPropertyFromJson(JsonObject object, String property) {
        if (object.get(property) != null) {
            return object.get(property).getAsLong();
        } else {
            return null;
        }
    }

}
