package models.planning;

import org.junit.Test;
import play.test.UnitTest;

/**
 * Unit tests for {@link Slot}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class SlotTest extends UnitTest {
    
    @Test
    public void getMinutes() {
        for (Slot slot : Slot.values()) {
            assertTrue(slot.getDuration().getMinutes() >= 60);
        }
    }
    
    @Test
    public void testToString() {
        assertEquals("9H00 - 10H00", Slot.NineTen.toString());
    }
}
