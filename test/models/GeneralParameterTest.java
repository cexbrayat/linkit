package models;

import org.junit.*;

/**
 * Unit tests for {@link GeneralParameter} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class GeneralParameterTest extends BaseDataUnitTest {
    
    @Test
    public void test() {
        final String key = "toto";
        assertNull(GeneralParameter.get(key));
        
        GeneralParameter.set(key, "1234");
        assertEquals("1234", GeneralParameter.get(key));
        
        GeneralParameter.set(key, "9876");
        assertEquals("9876", GeneralParameter.get(key));
    }
}
