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
        // CLA 10/04/2012 : Fix https://play.lighthouseapp.com/projects/57987/tickets/425-error-in-reloading-a-yml-file before Play! 1.2.5 release.
        // cf. http://stackoverflow.com/questions/6266684/cannot-load-fixture-because-of-wrong-duplicate-id-detection
        Fixtures.deleteDatabase();
        Fixtures.loadModels("data.yml");
    }

    @After
    public void tearDown() {
        Fixtures.deleteAllModels();
    }    
}
