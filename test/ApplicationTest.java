import org.junit.*;
import org.junit.After;
import org.junit.Before;
import play.test.*;
import play.mvc.Http.*;

public class ApplicationTest extends FunctionalTest {

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
    public void testThatIndexPageWorks() {
        Response response = GET("/");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }
/*
    @Test
    public void testThatProfilePageWorks() {
        // Need to be conected...
        assertIsOk(GET("/profile/ced"));
    }
*/
    @Test
    public void testProfileNotConnected() {
        assertStatus(302, GET("/profile/ced"));
    }
}