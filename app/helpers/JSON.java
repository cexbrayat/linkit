package helpers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * JSON tools
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class JSON {
    
    private JSON() {};
    
    public static JsonObject getAsObject(final String data) {
        JsonElement element = getAsElement(data);
        return element.getAsJsonObject();
    }

    public static JsonElement getAsElement(final String data) {
        return new JsonParser().parse(data);
    }
    
    public static String getStringProperty(JsonObject object, String property) {
        if (object.get(property) != null) {
            return object.get(property).getAsString();
        } else {
            return null;
        }
    }
    
    public static Long getLongProperty(JsonObject object, String property) {
        if (object.get(property) != null) {
            return object.get(property).getAsLong();
        } else {
            return null;
        }
    }
}
