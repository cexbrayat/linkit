package helpers.ticketing;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import helpers.JSON;
import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import models.Member;
import play.Logger;
import play.Play;
import play.libs.WS;
import play.libs.WS.HttpResponse;

/**
 * WeezEvent provider
 * @author Agnes <agnes.crepet@gmail.com>
 */
public class WeezEvent {

    public static String login() {
        final String weezevent_url = Play.configuration.getProperty("weezevent_url");
        final String weezevent_login = Play.configuration.getProperty("weezevent_login");
        final String weezevent_pw = Play.configuration.getProperty("weezevent_pw");
        final String weezevent_lang = Play.configuration.getProperty("weezevent_lang");
        return login(weezevent_url, weezevent_login, weezevent_pw, weezevent_lang);
    }

    public static String login(String weezevent_url, String weezevent_login, String weezevent_pw, String weezevent_lang) {
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
            return null;
        }

        if (JSON.getStringProperty(object, "stat").equals("0")) {
            Logger.error("Problem while sending a request to WeezEvent WebService for login with user %s. Code error=%s. Cookie=%s", weezevent_login, JSON.getStringProperty(object, "data"), cookie);
        }
        return getSessionId(cookie);
    }

    public static boolean logout(String sessionId) {
        return get("logout", sessionId);

    }

    public static boolean isLogged(String sessionId) {
        return get("isLogged", sessionId);
    }

    public static boolean setEvent(String sessionId) {
        final String weezevent_event = Play.configuration.getProperty("weezevent_event");
        return setEvent(weezevent_event, sessionId);
    }

    public static boolean setEvent(String weezevent_event, String sessionId) {
        final String weezevent_url = Play.configuration.getProperty("weezevent_url");
        StringBuilder url = new StringBuilder();
        url.append(weezevent_url).append("?setEvent=").append(weezevent_event);
        JsonObject object = null;
        try {
            object = JSON.getAsObject(WS.url(url.toString()).setHeader("Cookie", sessionId).post().getString());
        } catch (RuntimeException e) {
            Logger.error(e, "Exception while sending a request to WeezEvent WebService for setEvent with id event %s", weezevent_event);
            return false;
        }
        if (JSON.getStringProperty(object, "stat").equals("0")) {
            Logger.error("Problem while sending a request to WeezEvent WebService for setEvent with id event %s. Code error=%s. Complete URL=%s", weezevent_event, JSON.getStringProperty(object, "data"), url.toString());
            return false;
        }
        return true;
    }

    public static List<String> getAttendees(String sessionId) {
        final String weezevent_url = Play.configuration.getProperty("weezevent_url");
        StringBuilder url = new StringBuilder();
        url.append(weezevent_url).append("?getRegistered");
        JsonObject object = null;
        try {
            object = JSON.getAsObject(WS.url(url.toString()).setHeader("Cookie", sessionId).get().getString());
        } catch (RuntimeException e) {
            Logger.error(e, "Exception while sending a request to WeezEvent WebService for getAttendees");
            return null;
        }
        if (JSON.getStringProperty(object, "stat").equals("0")) {
            Logger.error("Problem while sending a request to WeezEvent WebService for getAttendees. Code error=%s", JSON.getStringProperty(object, "data"));
            return null;
        }
        JsonArray recs = JSON.getArrayProperty(object, "data");
        Iterator it = recs.iterator();
        List<String> emailsAllAttendees = new ArrayList<String>();
        while (it.hasNext()) {
            JsonObject rec = (JsonObject) it.next();
            String email_rec = JSON.getStringProperty(rec, "email");
            emailsAllAttendees.add(email_rec);
        }
        return emailsAllAttendees;
    }

    public static boolean isRegisteredAttendee(String email, List<String> emailsAllAttendees) {
        if (emailsAllAttendees != null && emailsAllAttendees.contains(email)) {
            return true;
        }
        return false;
    }

    public static void updateRegisteredAttendee(Member member) {
        String sessionID = WeezEvent.login();
        WeezEvent.setEvent(sessionID);
        final List<String> allAttendees = WeezEvent.getAttendees(sessionID);
        if (WeezEvent.isRegisteredAttendee(member.email, allAttendees)) {
            member.ticketingRegistered = true;
            member.save();
        }

    }

    public static String getSessionId(String cookie) {
        // recuperation du cookie PHPSESSID, cookie de l'API de weezevent
        Pattern pattern = Pattern.compile("PHPSESSID=(\\p{XDigit}+);");
        Matcher matcher = pattern.matcher(cookie);
        matcher.find();
        return matcher.group();
    }

    public static boolean get(String parameter, String sessionId) {
        final String weezevent_url = Play.configuration.getProperty("weezevent_url");
        StringBuilder url = new StringBuilder();
        url.append(weezevent_url).append("?").append(parameter);
        JsonObject object = null;
        try {
            object = JSON.getAsObject(WS.url(url.toString()).setHeader("Cookie", sessionId).get().getString());
        } catch (RuntimeException e) {
            Logger.error(e, "Exception while sending a request to WeezEvent WebService for %s", parameter);
            return false;
        }
        if (JSON.getStringProperty(object, "stat").equals("0")) {
            Logger.error("Problem while sending a request to WeezEvent WebService for %s. Stat=%s. Code error=%s. sessionId=%s.", parameter, JSON.getStringProperty(object, "stat"), JSON.getStringProperty(object, "data"), sessionId);
            return false;
        }
        return true;
    }
}
