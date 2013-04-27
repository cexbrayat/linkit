package helpers.ticketing;

import models.Member;
import org.junit.Ignore;
import org.junit.Test;
import play.Play;
import play.test.UnitTest;

import java.lang.Exception;
import java.lang.RuntimeException;
import java.util.Set;

/**
 * Unit tests for {@link helpers.ticketing.YurPlan}
 * Pour ces tests on presuppose d'un user contact@mix-it.fr a un billet pour l'event MIX-IT 2013 sous YurPlan
 * @author agnes <agnes.crepet@gmail.com>
 */
public class YurPlanTest extends UnitTest {

    protected static Member createMember(final String email) {
        Member member = new Member(email);
        member.email = email;
        member.save();
        return member;
    }
    @Test
    public void testLoginOK() {
        //identifiants YurPlan corrects
        assertNotNull(YurPlan.login());
    }

    @Test
    public void testLoginKO() {
        final String yurplan_url = Play.configuration.getProperty("yurplan.api.url");
        //identifiants YurPlan INcorrects
        assertNull(YurPlan.login(yurplan_url, "toto@toto.fr", "toto", "fr"));
    }

    @Test
    public void testIsRegisteredAttendeeOK() {
        String token = YurPlan.login();
        //the attendee with email toto@toto.fr does not exist in YurPlan
        assertTrue(YurPlan.isRegisteredAttendee(createMember("agnes007@no-log.org"), token));
    }

    @Test
    public void testIsRegisteredAttendeeKO() {
        String token = YurPlan.login();
        //the attendee with email toto@toto.fr does not exist in YurPlan
        assertFalse(YurPlan.isRegisteredAttendee(createMember("toto@toto.fr"), token));
    }


    @Test
    public void testREGISTRATION_URL() {
        assertNotNull(YurPlan.REGISTRATION_URL);
    }
}
