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
    public void testAllIconUrl() {
        for (Badge b : Badge.values()) {
            assertNotNull(b.getIconUrl());
            assertNotNull("L'image associée au badge "+b+" n'existe pas : " + b.getIconUrl(), Router.reverse(VirtualFile.fromRelativePath(b.getIconUrl())));
            final String badgePropertyKey = "badge."+b;
            assertFalse("Le texte associé au badge "+b+" n'existe pas dans le fichier messages", badgePropertyKey.equals(Messages.get(badgePropertyKey)));
        }
    }
}