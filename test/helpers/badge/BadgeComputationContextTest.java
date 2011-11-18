package helpers.badge;

import models.Speaker;
import models.Staff;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

/**
 * Unit tests for {@link helpers.badge.BadgeComputationContext}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class BadgeComputationContextTest extends UnitTest {

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
    public void getNbStaff() {
        final Long actualNbStaff = new BadgeComputationContext().getNbStaff();
        assertNotNull(actualNbStaff);
        assertEquals(Staff.count(), actualNbStaff.longValue());
    }
    
    @Test
    public void getNbSpeaker() {
        final Long actualNbSpeaker = new BadgeComputationContext().getNbSpeakers();
        assertNotNull(actualNbSpeaker);
        assertEquals(Speaker.count(), actualNbSpeaker.longValue());
    }
}
