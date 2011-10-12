package controllers.oauth;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controllers.Login;
import models.ProviderType;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import play.Play;
import play.libs.WS;
import play.mvc.Http;

/**
 * Default implementation of {@link OAuthProvider}
 * @author Sryl <cyril.lacote@gmail.com>
 */
abstract class AbstractOAuthProviderImpl implements OAuthProvider {

    private ProviderType provider;

    private OAuthService service;

    AbstractOAuthProviderImpl(ProviderType provider) {
        this.provider = provider;
        this.service = buildService();
    }

    public OAuthService getService() {
        return service;
    }

    /**
     * Create and configure OAuth service for provider
     * @return OAuthService
     */
    protected abstract OAuthService buildService();

    protected String getCallbackUrl() {
        return Login.getCallbackUrl(provider);
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

    /**
     * GET a HTTP resource with OAuth authentication
     * @param url
     * @param token
     * @param secret
     * @return 
     */
    public String get(String url, String token, String secret) {
        OAuthRequest request = new OAuthRequest(Verb.GET, url);
        service.signRequest(new Token(token, secret), request); // the access token from step 4
        Response response = request.send();
        if (response.isSuccessful()) {
            return response.getBody();
        } else {
            return null;
        }
    }

    /**
     * GET a HTTP resource without OAuth authentication
     * @param url
     * @param token
     * @param secret
     * @return 
     */
    public String get(String url) {
        return WS.url(url).get().getString();
    }

    protected Response post(OAuthRequest request, String token, String secret) {
        Token accessToken = new Token(token, secret);
        getService().signRequest(accessToken, request);
        Response response = request.send();
        if (response.getCode() != Http.StatusCode.OK) {
            throw new OAuthProviderException(response.getCode(), response.getBody(), provider);
        }

        return response;
    }

    static JsonObject getAsJsonObject(final String data) {
        JsonElement element = getAsJsonElement(data);
        return element.getAsJsonObject();
    }

    static JsonElement getAsJsonElement(final String data) {
        return new JsonParser().parse(data);
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
