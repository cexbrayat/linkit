package helpers.ticketing;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import helpers.JSON;
import models.Member;
import play.Logger;
import play.Play;
import play.libs.WS;
import play.libs.WS.HttpResponse;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * YurPlan ticketing provider
 *
 * @author Agnes <agnes.crepet@gmail.com>
 */
public class YurPlan {


    private static final String API_URL = Play.configuration.getProperty("yurplan.api.url");
    private static final String API_EMAIL = Play.configuration.getProperty("yurplan.api.email");
    private static final String API_PWD = Play.configuration.getProperty("yurplan.api.pwd");
    private static final String API_KEY = Play.configuration.getProperty("yurplan.api.key");
    private static final String EVENT = Play.configuration.getProperty("yurplan.event");

    /**
     * User URL for ticketing
     */
    public static final String REGISTRATION_URL = Play.configuration.getProperty("yurplan.url");

    public static String login() {
        return login(API_URL, API_EMAIL, API_PWD, API_KEY);
    }

    /**
     * @param yurplan_url
     * @param yurplan_email
     * @param yurplan_pw
     * @param yurplan_key
     * @return token
     * @throws Exception
     */
    protected static String login(String yurplan_url, String yurplan_email, String yurplan_pw, String yurplan_key) {
        StringBuilder url = new StringBuilder();
        //yurplan.com/api.php/auth?key=xxx email=contact@mix-it.fr password=xxx
        url.append(yurplan_url).append("auth?key=").append(yurplan_key);

        JsonElement object = null;
        String token = null;
        try {
            WS.WSRequest wsRequest = WS.url(url.toString());
            wsRequest.setParameter("email", yurplan_email);
            wsRequest.setParameter("password", yurplan_pw);
            HttpResponse response = wsRequest.post();
            object = response.getJson();
            Logger.info("getContentType: %s",response.getContentType());
            Logger.info("yurplan_email: %s",yurplan_email);
            Logger.info("yurplan_pw: %s",yurplan_pw);
            Logger.info("url: %s",url);
            Logger.info("getStatus: %s",response.getStatus());
            Logger.info("JSON: %s",object);

        } catch (RuntimeException e) {
            Logger.error(e, "Exception while sending a request to YurPlan WebService for login with email %s", yurplan_email);
        }

//        if (!object.getAsJsonObject().getAsString("code").equals("200")) {
//            Logger.error("Problem while sending a request to YurPlan WebService for login with email %s. " +
//                    "API_URL=%s - Key=%s - Password=%s Code error=%s - Result=%s",
//                    yurplan_email, yurplan_url, yurplan_key, yurplan_pw, JSON.getStringProperty(object, "code"), JSON.getStringProperty(object, "result"));
//        }
        JsonArray recs = object.getAsJsonObject().getAsJsonArray("data");
        if (recs != null) {
            Iterator it = recs.iterator();
            while (it.hasNext()) {
                JsonObject rec = (JsonObject) it.next();
                token = JSON.getStringProperty(rec, "token");
            }
        }
        return token;
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
        try {
            YurPlan.login();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        // YurPlan.setEvent(sessionID);
        // final Set<String> allAttendees = YurPlan.getAttendees(sessionID);
        // member.setTicketingRegistered(YurPlan.isRegisteredAttendee(member.email, allAttendees));
        // member.save();

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
