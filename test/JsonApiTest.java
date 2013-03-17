import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http.Response;
import play.test.Fixtures;
import play.test.FunctionalTest;

public class JsonApiTest extends FunctionalTest {

    @Before
    public void setUp() {
        Fixtures.deleteAllModels();
        Fixtures.loadModels("init-data.yml");
    }

    @After
    public void tearDown() {
        Fixtures.deleteAllModels();
    }

    @Test
    public void testSpeakers() {
        Response response = GET("/api/speakers");
        assertJson(response);
    }

    @Test
    public void testSponsors() {
        Response response = GET("/api/sponsors");
        assertJson(response);
    }

    @Test
    public void testMembers() {
        Response response = GET("/api/members");
        assertJson(response);
    }

    @Test
    public void testTalks() {
        Response response = GET("/api/talks");
        assertJson(response);
    }

    @Test
    public void testLightningalks() {
        Response response = GET("/api/lightningtalks");
        assertJson(response);
    }

    @Test
    public void testInterests() {
        Response response = GET("/api/interests");
        assertJson(response);
    }

    private void assertJson(Response response) {
        assertIsOk(response);
        assertContentType("application/json", response);
        assertFalse(getContent(response).isEmpty());
    }
}