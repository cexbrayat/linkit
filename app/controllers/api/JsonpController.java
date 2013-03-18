package controllers.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import play.mvc.Controller;
import play.mvc.Util;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Add JSONP support : render callback function if "callback" parameter provided.
 */
public class JsonpController extends Controller {

    public static final String CALLBACK_FORMAT = "%s(%s)";

    @Util
    protected static void renderJSON(String str) {
        String callback = getCallbackParameter();
        if (callback != null) {
            Controller.renderJSON(String.format(CALLBACK_FORMAT, callback, str));
        } else {
            Controller.renderJSON(str);
        }
    }

    @Util
    protected static void renderJSON(Object o, JsonSerializer... adapters) {
        String callback = getCallbackParameter();
        if (callback != null) {
            Controller.renderJSON(String.format(CALLBACK_FORMAT, callback, createGson(adapters).toJson(o)));
        } else {
            Controller.renderJSON(o, adapters);
        }
    }

    @Util
    protected static void renderJSON(Object o) {
        String callback = getCallbackParameter();
        if (callback != null) {
            Controller.renderJSON(String.format(CALLBACK_FORMAT, callback, createGson().toJson(o)));
        } else {
            Controller.renderJSON(o);
        }
    }

    private static String getCallbackParameter() {
        if (request.params._contains("callback")) {
            return request.params.get("callback");
        }
        return null;
    }

    private static Gson createGson(JsonSerializer... adapters) {
        GsonBuilder gson = new GsonBuilder();
        if (adapters != null) {
            for (Object adapter : adapters) {
                Type t = getMethod(adapter.getClass(), "serialize").getParameterTypes()[0];
                gson.registerTypeAdapter(t, adapter);
            }
        }
        return gson.create();
    }

    static Method getMethod(Class<?> clazz, String name) {
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getName().equals(name)) {
                return m;
            }
        }
        return null;
    }
}
