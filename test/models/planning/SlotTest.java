package models.planning;

import org.joda.time.Minutes;
import org.junit.Test;
import play.test.UnitTest;

/**
 * Unit tests for {@link Slot}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class SlotTest extends UnitTest {
    
    @Test
    public void getDuration() {
        assertEquals(Minutes.minutes(60), Slot.NineTen.getDuration());
    }
    
    @Test
    public void testToString() {
        assertEquals("9H00 - 10H00", Slot.NineTen.toString());
    }
    
    @Test
    public void getStartMinutesFromMidnight() {
        assertEquals(9*60, Slot.NineTen.getStartMinutesFromMidnight());
    }
    
    @Test
    public void getEndMinutesFromMidnight() {
        assertEquals(10*60, Slot.NineTen.getEndMinutesFromMidnight());
    }
}
