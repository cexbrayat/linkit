import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;
import play.vfs.VirtualFile;

/**
 * Functional tests for {@link Badge}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class BadgeFunctionalTest extends FunctionalTest {
    
    @Test
    public void testReverseUrl() {
        String url = Router.reverse(VirtualFile.fromRelativePath(Badge.Speaker.getIconUrl()));
        assertNotNull(url);
    }

    @Test
    @Ignore // FIXME Play! doesn't serve static content in functional test...
    public void testIconUrl() {
        for (Badge b : Badge.values()) {
            assertNotNull(b.getIconUrl());
            // FIXME A java.lang.RuntimeException has been caught, java.util.concurrent.ExecutionException: play.exceptions.UnexpectedException: Unexpected Error
            // We're fucked : Play! doesn't support serving static resource in functional test, cf. http://groups.google.com/group/play-framework/browse_thread/thread/203dc14f8a45ea81?pli=1
            Response response = GET(b.getIconUrl());
            assertIsOk(response);
        }
    }
}