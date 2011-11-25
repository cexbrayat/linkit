package models.auth;

import models.ProviderType;
import org.junit.*;
import play.test.*;

/**
 * Unit tests for {@link AuthAccount} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class AuthAccountTest extends UnitTest {

    @Before
    public void setUp() {
        Fixtures.deleteAllModels();
        Fixtures.loadModels("data.yml");
    }

    @After
    public void tearDown() {
        Fixtures.deleteAllModels();
    }

    @Test
    public void find() {
        final String login = "ced";
        assertNotNull(AuthAccount.find(ProviderType.LinkIt, login));
    }
}
