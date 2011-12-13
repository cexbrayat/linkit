package models.generator;

import org.junit.Test;
import play.test.UnitTest;

/**
 * Unit tests for {@link DataGenerator}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class DummyTest extends UnitTest {
    
    @Test
    public void randomInt() {
        final int max = 1000;
        for (int i = 0; i < 100000; i++) {
            int random = Dummy.randomInt(max);
            assertTrue(random >= 0);
            assertTrue(random < max);
        }
    }
}
