package helpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * JSON tools
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class JSON {

    private JSON() {
    }

    ;
    
    public static JsonObject getAsObject(final String data) {
        JsonElement element = getAsElement(data);
        return element.getAsJsonObject();
    }

    public static JsonElement getAsElement(final String data) {
        return new JsonParser().parse(data);
    }

    public static String getStringProperty(JsonObject object, String property) {
        return (getProperty(object, property) != null) ? getProperty(object, property).getAsString() : null;
    }

    public static JsonArray getArrayProperty(JsonObject object, String property) {
        return (getProperty(object, property) != null) ? getProperty(object, property).getAsJsonArray() : null;
    }

    public static Long getLongProperty(JsonObject object, String property) {
        return (getProperty(object, property) != null) ? getProperty(object, property).getAsLong() : null;
    }

    private static JsonElement getProperty(JsonObject object, String property) {
        if (object.get(property) != null && !object.get(property).isJsonNull()) {
            return object.get(property);
        } else {
            return null;
        }
    }
}
