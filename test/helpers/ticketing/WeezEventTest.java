package helpers.ticketing;

import com.google.gson.JsonArray;
import org.junit.Before;
import org.junit.Test;
import play.Play;
import play.test.UnitTest;

/**
 * Unit tests for {@link WeezEvent}
 * @author agnes <agnes.crepet@gmail.com>
 */
public class WeezEventTest extends UnitTest {

    WeezEvent we;

    @Before
    public void before() {
        we = new WeezEvent();
    }

    @Test
    public void testLoginOK() {
        //identifiants weezevent corrects
        String sessionID = we.login();
        assertTrue(we.isLogged(sessionID));
        we.logout(sessionID);
    }

    @Test
    public void testLoginKO() {
        final String weezevent_url = Play.configuration.getProperty("weezevent_url");
        //identifiants weezevent INcorrects
        String sessionID = we.login(weezevent_url, "toto@toto.fr", "toto", "fr");
        assertFalse(we.isLogged(sessionID));
    }

    @Test
    public void testSetEventOK() {
        String sessionID = we.login();
        //event id correct
        assertTrue(we.setEvent(sessionID));
        we.logout(sessionID);
    }

    @Test
    public void testSetEventKO() {
        //identifiants weezevent corrects
        String sessionID = we.login();
        //event id INcorrect
        assertFalse(we.setEvent("00000", sessionID));
        we.logout(sessionID);
    }

    @Test
    public void testGetAttendeesOK() {
        //identifiants weezevent corrects
        String sessionID = we.login();
        //event id correct
        we.setEvent(sessionID);
        assertNotNull(we.getAttendees(sessionID));
    }

    @Test
    public void testGetAttendeesKO() {
        //identifiants weezevent corrects
        String sessionID = we.login();
        //event id INcorrect
        we.setEvent("00000", sessionID);
        assertNull(we.getAttendees(sessionID));
    }

    @Test
    public void testIsRegisteredAttendeeOK() {
        //identifiants weezevent corrects
        String sessionID = we.login();
        //event id correct
        we.setEvent(sessionID);
        JsonArray allAttendees = we.getAttendees(sessionID);
        assertTrue(we.isRegisteredAttendee("agnes.crepet@gmail.com", allAttendees));
    }

    @Test
    public void testIsRegisteredAttendeeKO() {
        //identifiants weezevent corrects
        String sessionID = we.login();
        //event id correct
        we.setEvent(sessionID);
        JsonArray allAttendees = we.getAttendees(sessionID);
        assertFalse(we.isRegisteredAttendee("toto.toto@gmail.com", allAttendees));
    }
}
