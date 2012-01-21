package models;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import play.test.UnitTest;

/**
 * Unit tests for {@link Track} domain object.
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class TrackTest extends UnitTest {
    
    @Test public void getDescription() {
        for (Track t : Track.values()) {
            assertFalse("La track " + t.name() + " n'a pas de description configurée dans les messages", StringUtils.isBlank(t.getDescription()));
        }
    }
}
