import org.junit.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;
import play.i18n.Messages;
import play.test.FunctionalTest;
import play.vfs.VirtualFile;

/**
 * Functional tests for {@link Badge} : ensuring that all static resources are there before trying to serve them to end user.
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class BadgeFunctionalTest extends FunctionalTest {

    @Test
    public void testAllDisplayable() {
        for (Badge b : Badge.values()) {
            // Every badge must have an icon as image file URL or Unicode character
            assertTrue(b.getIconUrl() != null || b.getIconChar() != null);
            if (b.getIconUrl() != null) {
                // If icon as image file URL, ensure image is served by Play!
                assertNotNull("L'image associée au badge "+b+" n'existe pas : " + b.getIconUrl(), Router.reverse(VirtualFile.fromRelativePath(b.getIconUrl())));
            }
            // Ensure existing message for badge title
            final String badgePropertyKey = "badge."+b;
            assertFalse("Le texte associé au badge "+b+" n'existe pas dans le fichier messages", badgePropertyKey.equals(Messages.get(badgePropertyKey)));
        }
    }
}