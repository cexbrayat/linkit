package models;


import org.junit.After;
import org.junit.Before;
import play.test.Fixtures;
import play.test.UnitTest;

/**
 * Base class for all unit tests using default data in DB.
 * @author Sryl <cyril.lacote@gmail.com>
 */
public abstract class BaseDataUnitTest extends UnitTest {

    @Before
    public void setUp() {
        Fixtures.deleteAllModels();
        Fixtures.loadModels("data.yml");
    }

    @After
    public void tearDown() {
        Fixtures.deleteAllModels();
    }    
}
