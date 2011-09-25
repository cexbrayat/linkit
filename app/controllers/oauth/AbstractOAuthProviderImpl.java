package controllers.oauth;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import play.Play;
import play.libs.OAuth.ServiceInfo;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public abstract class AbstractOAuthProviderImpl implements OAuthProvider {

    static protected ServiceInfo getServiceInfo(String provider) {
        final String requestTokenURL = Play.configuration.getProperty(provider + ".requestTokenUrl");
        final String accessTokenURL = Play.configuration.getProperty(provider + ".accessTokenUrl");
        final String authorizeURL = Play.configuration.getProperty(provider + ".authorizeUrl");
        final String consumerKey = Play.configuration.getProperty(provider + ".consumerKey");
        final String consumerSecret = Play.configuration.getProperty(provider + ".consumerSecret");
        return new ServiceInfo(requestTokenURL, accessTokenURL, authorizeURL, consumerKey, consumerSecret);
    }

    static String getStringPropertyFromJson(JsonElement response, String property) {
        JsonObject jObjet = response.getAsJsonObject();
        if (jObjet.get(property) != null) {
            return jObjet.get(property).getAsString();
        } else {
            return null;
        }
    }

    static String getStringPropertyFromJson(JsonObject object, String property) {
        if (object.get(property) != null) {
            return object.get(property).getAsString();
        } else {
            return null;
        }
    }

    static Long getLongPropertyFromJson(JsonElement response, String property) {
        JsonObject jObjet = response.getAsJsonObject();
        if (jObjet.get(property) != null) {
            return jObjet.getAsLong();
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
