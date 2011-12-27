package helpers.ticketing;

import com.google.gson.JsonElement;
import java.lang.String;
import java.util.Map;
import play.Play;
import play.libs.WS;

/**
 * WeezEvent provider
 * @author Agnes <agnes.crepet@gmail.com>
 */
public class WeezEvent{

    public void login(String login, String secret, String lang) {
        
        final String weezevent_url = Play.configuration.getProperty("weezevent_url");
        final String weezevent_login = Play.configuration.getProperty("weezevent_login");
        final String weezevent_pw = Play.configuration.getProperty("weezevent_pw");
        final String weezevent_lang = Play.configuration.getProperty("weezevent_lang");

        final JsonElement element= WS.url(weezevent_url).setParameter("login", weezevent_login)
                .setParameter("pw", weezevent_pw)
                .setParameter("lang", weezevent_lang).post().getJson();

    }


}
