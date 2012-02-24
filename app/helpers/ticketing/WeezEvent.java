package helpers.ticketing;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import helpers.JSON;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
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


    private static final String API_URL = Play.configuration.getProperty("weezevent.api.url");
    private static final String API_LOGIN = Play.configuration.getProperty("weezevent.api.login");
    private static final String API_PWD = Play.configuration.getProperty("weezevent.api.pwd");
    private static final String API_LANG = Play.configuration.getProperty("weezevent.api.lang");
    private static final String EVENT = Play.configuration.getProperty("weezevent.event");
    private static final String CODE = Play.configuration.getProperty("weezevent.code");

    /** User URL for ticketing */
    public static final String REGISTRATION_URL = String.format(Play.configuration.getProperty("weezevent.url"), EVENT, CODE);
    
    public static String login() {
        return login(API_URL, API_LOGIN, API_PWD, API_LANG);
    }

    protected static String login(String weezevent_url, String weezevent_login, String weezevent_pw, String weezevent_lang) {
        StringBuilder url = new StringBuilder();
        url.append(weezevent_url).append("?login=").append(weezevent_login).append("&pw=").append(weezevent_pw).append("&lang=").append(weezevent_lang).append("&t=timestamp");
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
        return setEvent(EVENT, sessionId);
    }

    protected static boolean setEvent(String event, String sessionId) {
        StringBuilder url = new StringBuilder();
        url.append(API_URL).append("?setEvent=").append(event);
        JsonObject object = null;
        try {
            object = JSON.getAsObject(WS.url(url.toString()).setHeader("Cookie", sessionId).post().getString());
        } catch (RuntimeException e) {
            Logger.error(e, "Exception while sending a request to WeezEvent WebService for setEvent with id event %s", EVENT);
            return false;
        }
        if (JSON.getStringProperty(object, "stat").equals("0")) {
            Logger.error("Problem while sending a request to WeezEvent WebService for setEvent with id event %s. Code error=%s. Complete URL=%s", EVENT, JSON.getStringProperty(object, "data"), url.toString());
            return false;
        }
        return true;
    }

    public static Set<String> getAttendees(String sessionId) {
        StringBuilder url = new StringBuilder();
        url.append(API_URL).append("?getRegistered");
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
        Set<String> emailsAllAttendees = new HashSet<String>();
        while (it.hasNext()) {
            JsonObject rec = (JsonObject) it.next();
            String email_rec = JSON.getStringProperty(rec, "email");
            emailsAllAttendees.add(email_rec);
        }
        return emailsAllAttendees;
    }

    public static boolean isRegisteredAttendee(String email, Set<String> emailsAllAttendees) {
        if (emailsAllAttendees != null && emailsAllAttendees.contains(email)) {
            return true;
        }
        return false;
    }

    public static void updateRegisteredAttendee(Member member) {
        String sessionID = WeezEvent.login();
        WeezEvent.setEvent(sessionID);
        final Set<String> allAttendees = WeezEvent.getAttendees(sessionID);
        member.setTicketingRegistered(WeezEvent.isRegisteredAttendee(member.email, allAttendees));
        member.save();

    }

    public static String getSessionId(String cookie) {
        // recuperation du cookie PHPSESSID, cookie de l'API de weezevent
        Pattern pattern = Pattern.compile("PHPSESSID=(\\p{XDigit}+);");
        Matcher matcher = pattern.matcher(cookie);
        matcher.find();
        return matcher.group();
    }

    public static boolean get(String parameter, String sessionId) {
        StringBuilder url = new StringBuilder();
        url.append(API_URL).append("?").append(parameter);
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
