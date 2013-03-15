package helpers.ticketing;

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
@Ignore
public class YurPlanTest extends UnitTest {

    @Test
    public void testLoginOK() {
        //identifiants weezevent corrects
        assertNotNull(YurPlan.login());
    }

    @Test
    @Ignore
    public void testLoginKO() {
        final String yurplan_url = Play.configuration.getProperty("yurplan.url");
        //identifiants weezevent INcorrects
        assertNull(YurPlan.login(yurplan_url, "toto@toto.fr", "toto", "fr"));
    }


    @Test
    @Ignore
    public void testREGISTRATION_URL() {
        assertNotNull(YurPlan.REGISTRATION_URL);
    }
}
