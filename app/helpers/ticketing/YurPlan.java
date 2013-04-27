package helpers.ticketing;

import com.google.gson.JsonObject;
import helpers.JSON;
import models.Member;
import play.Logger;
import play.Play;
import play.libs.WS;

/**
 * YurPlan ticketing provider
 *
 * @author Agnes <agnes.crepet@gmail.com>
 */
public class YurPlan {


    private static final String API_URL = Play.configuration.getProperty("yurplan.api.url");
    private static final String API_EMAIL = Play.configuration.getProperty("yurplan.api.email");
    private static final String API_PWD = Play.configuration.getProperty("yurplan.api.password");
    private static final String API_KEY = Play.configuration.getProperty("yurplan.api.key");
    private static final String EVENT = Play.configuration.getProperty("yurplan.api.event");

    /**
     * User URL for ticketing
     */
    public static final String REGISTRATION_URL = Play.configuration.getProperty("yurplan.url");
    private String token;

    public static String login() {
        return login(API_URL, API_EMAIL, API_PWD, API_KEY);
    }

    public static boolean isRegisteredAttendee(Member member, String token) {
        return isRegisteredAttendee(member.email, token, API_URL, API_KEY, EVENT);
    }

    /**
     * Login to YurPlan
     *
     * @param yurplan_url
     * @param yurplan_email
     * @param yurplan_pw
     * @param yurplan_key
     * @return token
     * @throws Exception
     */
    protected static String login(String yurplan_url, String yurplan_email, String yurplan_pw, String yurplan_key) {
        StringBuilder url = new StringBuilder();
        url.append(yurplan_url).append("auth?key=").append(yurplan_key);
        StringBuilder body = new StringBuilder();
        body.append("{\"email\":\"").append(yurplan_email).append("\",\"password\":\"").append(yurplan_pw).append("\"}");
        JsonObject object = null;
        String token = null;
        try {
            object = JSON.getAsObject(WS.url(url.toString())
                    .setHeader("Content-type", "application/json")
                    .body(body)
                    .post().getString());

        } catch (RuntimeException e) {
            Logger.error(e, "Exception while sending a request to Yurplan WebService for login with email : %s", yurplan_email);
        }

        String codeRequest = JSON.getStringProperty(object, "code");
        if (codeRequest != null && codeRequest.equals("200")) {
            JsonObject data = JSON.getObject(object, "data");
            token = JSON.getStringProperty(data, "token");
        } else {
            Logger.error("Exception while sending a request to Yurplan WebService for login with email : %s . Code error = %s ", yurplan_email, codeRequest);
        }

        return token;
    }

    /**
     * Is the attendee with email_attendee has a ticket for the event yurplan_event
     *
     * @param email_attendee
     * @param token
     * @param yurplan_url
     * @param yurplan_key
     * @param yurplan_event
     * @return
     */
    protected static boolean isRegisteredAttendee(String email_attendee, String token, String yurplan_url, String yurplan_key, String yurplan_event) {
        StringBuilder url = new StringBuilder();
        url.append(yurplan_url).append("events/").append(yurplan_event)
                .append("/tickets/find?key=").append(yurplan_key)
                .append("&search=").append(email_attendee)
                .append("&token=").append(token);
        JsonObject object = null;
        Logger.info("url : %s", url);
        try {
            object = JSON.getAsObject(WS.url(url.toString())
                    .get().getString());

        } catch (RuntimeException e) {
            Logger.error(e, "Exception while sending a request to Yurplan WebService for search an attendee with email_attendee : %s", email_attendee);
        }

        String codeRequest = JSON.getStringProperty(object, "code");
        if (codeRequest != null && codeRequest.equals("200")) {
            JsonObject data = JSON.getObject(object, "data");
            String count = JSON.getStringProperty(data, "count");
            if (data != null && Integer.valueOf(count) > 0) {
                return true;
            }
        } else {
            Logger.error("Exception while sending a request to Yurplan WebService for search an attendee with email_attendee : %s . Code error = %s ", email_attendee, codeRequest);
        }
        return false;
    }

}
