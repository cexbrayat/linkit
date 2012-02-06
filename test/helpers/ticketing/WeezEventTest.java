package helpers.ticketing;

import java.util.List;
import org.junit.Test;
import play.Play;
import play.test.UnitTest;

/**
 * Unit tests for {@link WeezEvent}
 * Pour ces tests on presuppose d'un user contact@mix-it.fr a un billet pour l'event MIX-IT 2012 sous WeezEvent
 * @author agnes <agnes.crepet@gmail.com>
 */
public class WeezEventTest extends UnitTest {

    @Test
    public void testLoginOK() {
        //identifiants weezevent corrects
        String sessionID = WeezEvent.login();
        assertTrue(WeezEvent.isLogged(sessionID));
        WeezEvent.logout(sessionID);
    }

    @Test
    public void testLoginKO() {
        final String weezevent_url = Play.configuration.getProperty("weezevent_url");
        //identifiants weezevent INcorrects
        String sessionID = WeezEvent.login(weezevent_url, "toto@toto.fr", "toto", "fr");
        assertFalse(WeezEvent.isLogged(sessionID));
    }

    @Test
    public void testSetEventOK() {
        String sessionID = WeezEvent.login();
        //event id correct
        assertTrue(WeezEvent.setEvent(sessionID));
        WeezEvent.logout(sessionID);
    }

    @Test
    public void testSetEventKO() {
        //identifiants weezevent corrects
        String sessionID = WeezEvent.login();
        //event id INcorrect
        assertFalse(WeezEvent.setEvent("00000", sessionID));
        WeezEvent.logout(sessionID);
    }

    @Test
    public void testGetAttendeesOK() {
        //identifiants weezevent corrects
        String sessionID = WeezEvent.login();
        //event id correct
        WeezEvent.setEvent(sessionID);
        assertNotNull(WeezEvent.getAttendees(sessionID));
    }

    @Test
    public void testGetAttendeesKO() {
        //identifiants weezevent corrects
        String sessionID = WeezEvent.login();
        //event id INcorrect
        WeezEvent.setEvent("00000", sessionID);
        assertNull(WeezEvent.getAttendees(sessionID));
    }

    @Test
    public void testIsRegisteredAttendeeOK() {
        //identifiants weezevent corrects
        String sessionID = WeezEvent.login();
        //event id correct
        WeezEvent.setEvent(sessionID);
        List<String> allAttendees = WeezEvent.getAttendees(sessionID);
        assertTrue(WeezEvent.isRegisteredAttendee("agnes.crepet@gmail.com", allAttendees));
    }

    @Test
    public void testIsRegisteredAttendeeKO() {
        //identifiants weezevent corrects
        String sessionID = WeezEvent.login();
        //event id correct
        WeezEvent.setEvent(sessionID);
        List<String> allAttendees = WeezEvent.getAttendees(sessionID);
        assertFalse(WeezEvent.isRegisteredAttendee("toto.toto@gmail.com", allAttendees));
    }
}
