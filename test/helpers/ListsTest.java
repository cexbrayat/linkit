package helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import play.test.UnitTest;

/**
 * Unit tests for {@link Lists} helpers
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class ListsTest extends UnitTest {
    
    @Test public void putOnTop() {
        assertEquals(Arrays.asList(3, 1, 2, 4), Lists.putOnTop(Arrays.asList(1, 2, 3, 4), 3));
        assertEquals(Arrays.asList(3, 6, 1, 2, 4, 5, 7), Lists.putOnTop(Arrays.asList(1, 2, 3, 4, 5, 6, 7), 3, 6));
    }
    
    @Test public void putOnTopNull() {
        final List<Integer> list = new ArrayList<Integer>();
        assertEquals(list, Lists.putOnTop(list));
   }
    
    @Test public void putOnTopEmpty() {
        final List<Integer> list = new ArrayList<Integer>();
        assertEquals(list, Lists.putOnTop(list, new ArrayList<Integer>()));
    }
}
