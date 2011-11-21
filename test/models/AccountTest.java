package models;

import org.junit.*;
import play.test.*;

/**
 * Unit tests for {@link Account} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class AccountTest extends UnitTest {

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
        assertNotNull(Account.find(ProviderType.LinkIt, login));
    }
}
