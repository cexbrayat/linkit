import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

/**
 * Functional tests for {@link Badge}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class BadgeFunctionalTest extends FunctionalTest {

    @Test
    public void testIconUrl() {
        for (Badge b : Badge.values()) {
            assertNotNull(b.getIconUrl());
            // FIXME A java.lang.RuntimeException has been caught, java.util.concurrent.ExecutionException: play.exceptions.UnexpectedException: Unexpected Error
            Response response = GET(b.getIconUrl());
            assertIsOk(response);
        }
    }
}