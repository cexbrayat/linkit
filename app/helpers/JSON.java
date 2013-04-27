package helpers;

import com.google.gson.*;
import play.db.jpa.Model;

import java.util.Collection;

/**
 * JSON tools
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class JSON {

    private JSON() {
    }

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

    public static JsonObject getObject(JsonObject object, String property) {
        return (getProperty(object, property) != null) ? getProperty(object, property).getAsJsonObject() : null;
    }

    private static JsonElement getProperty(JsonObject object, String property) {
        if ((object != null) && (object.get(property) != null) && (!object.get(property).isJsonNull())) {
            return object.get(property);
        } else {
            return null;
        }
    }

    public static JsonArray toJsonArrayOfIds(Collection<? extends Model> entities) {
        JsonArray array = new JsonArray();
        for (Model e : entities) {
            array.add(new JsonPrimitive(e.id));
        }
        return array;
    }
}
