package helpers.ticketing;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import helpers.JSON;
import java.lang.String;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import play.Logger;
import play.Play;
import play.libs.WS;
import play.libs.WS.HttpResponse;

/**
 * WeezEvent provider
 * @author Agnes <agnes.crepet@gmail.com>
 */
public class WeezEvent {

    public String login() {
        final String weezevent_url = Play.configuration.getProperty("weezevent_url");
        final String weezevent_login = Play.configuration.getProperty("weezevent_login");
        final String weezevent_pw = Play.configuration.getProperty("weezevent_pw");
        final String weezevent_lang = Play.configuration.getProperty("weezevent_lang");
        return login(weezevent_url, weezevent_login, weezevent_pw, weezevent_lang);
    }

    public String login(String weezevent_url, String weezevent_login, String weezevent_pw, String weezevent_lang) {
        StringBuilder url = new StringBuilder();
        url.append(weezevent_url).append("?login=").append(weezevent_login).append("&").append("pw=").append(weezevent_pw).append("&").append("lang=").append(weezevent_lang).append("&t=timestamp");
        JsonObject object = null;
        String cookie = null;
        try {
            HttpResponse response = WS.url(url.toString()).get();
            cookie = response.getHeader("Set-Cookie");
            object = JSON.getAsObject(response.getString());

        } catch (RuntimeException e) {
            Logger.error(e, "Exception while sending a request to WeezEvent WebService for login with user %s", weezevent_login);
        }

        if (JSON.getStringProperty(object, "stat").equals("0")) {
            Logger.error("Problem while sending a request to WeezEvent WebService for login with user %s. Code error=%s. Cookie=%s", weezevent_login, JSON.getStringProperty(object, "data"), cookie);
        }
        return getSessionId(cookie);
    }

    public boolean logout(String sessionId) {
        return get("lougout", sessionId);

    }

    public boolean isLogged(String sessionId) {
        return get("isLogged", sessionId);
    }

    public boolean setEvent(String sessionId) {
        final String weezevent_event = Play.configuration.getProperty("weezevent_event");
        return setEvent(weezevent_event, sessionId);
    }

    public boolean setEvent(String weezevent_event, String sessionId) {
        final String weezevent_url = Play.configuration.getProperty("weezevent_url");
        StringBuilder url = new StringBuilder();
        url.append(weezevent_url).append("?setEvent=").append(weezevent_event);
        JsonObject object = null;
        try {
            object = JSON.getAsObject(WS.url(url.toString()).setHeader("Cookie", sessionId).post().getString());
        } catch (RuntimeException e) {
            Logger.error(e, "Exception while sending a request to WeezEvent WebService for setEvent with id event %s", weezevent_event);
        }
        if (JSON.getStringProperty(object, "stat").equals("0")) {
            Logger.error("Problem while sending a request to WeezEvent WebService for setEvent with id event %s. Code error=%s. Complete URL=%s", weezevent_event, JSON.getStringProperty(object, "data"), url.toString());
            return false;
        }
        return true;
    }

    public JsonArray getAttendees(String sessionId) {
        final String weezevent_url = Play.configuration.getProperty("weezevent_url");
        StringBuilder url = new StringBuilder();
        url.append(weezevent_url).append("?getRegistered");
        JsonObject object = null;
        try {
            object = JSON.getAsObject(WS.url(url.toString()).setHeader("Cookie", sessionId).get().getString());
        } catch (RuntimeException e) {
            Logger.error(e, "Exception while sending a request to WeezEvent WebService for getAttendees");
        }
        if (JSON.getStringProperty(object, "stat").equals("0")) {
            Logger.error("Problem while sending a request to WeezEvent WebService for getAttendees. Code error=%s", JSON.getStringProperty(object, "data"));
            return null;
        }
        return JSON.getArrayProperty(object, "data");
    }

    public boolean isRegisteredAttendee(String email, JsonArray recs) {
        Iterator it = recs.iterator();
        while (it.hasNext()) {
            JsonObject rec = (JsonObject) it.next();
            String email_rec = JSON.getStringProperty(rec, "email");
            if (email.equals(email_rec)) {
                return true;
            }
        }
        return false;
    }

    public String getSessionId(String cookie) {
        // recuperation du cookie PHPSESSID, cookie de l'API de weezevent
        Pattern pattern = Pattern.compile("PHPSESSID=(\\p{XDigit}+);");
        Matcher matcher = pattern.matcher(cookie);
        matcher.find();
        return matcher.group();
    }

    public boolean get(String parameter, String sessionId) {
        final String weezevent_url = Play.configuration.getProperty("weezevent_url");
        StringBuilder url = new StringBuilder();
        url.append(weezevent_url).append("?").append(parameter);
        JsonObject object = null;
        try {
            object = JSON.getAsObject(WS.url(url.toString()).setHeader("Cookie", sessionId).get().getString());
        } catch (RuntimeException e) {
            Logger.error(e, "Exception while sending a request to WeezEvent WebService for %s", parameter);
        }
        if (JSON.getStringProperty(object, "stat").equals("0")) {
            Logger.error("Problem while sending a request to WeezEvent WebService for %s. Stat=%s. Code error=%s. sessionId=%s.", parameter, JSON.getStringProperty(object, "stat"), JSON.getStringProperty(object, "data"), sessionId);
            return false;
        }
        return true;
    }
}
