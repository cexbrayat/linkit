package models.activity;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import play.test.Fixtures;
import play.test.UnitTest;

/**
 * Abstract class for units tests of {@link Activity} and derived classes.
 * @author Sryl <cyril.lacote@gmail.com>
 */
public abstract class AbstractActivityTest extends UnitTest {

    public static final String DEFAULT_LANG = "fr";

    @Before
    public void setUp() {
        Fixtures.deleteAllModels();
        Fixtures.loadModels("data.yml");
    }

    @After
    public void tearDown() {
        Fixtures.deleteAllModels();
    }

    protected void assertActivity(final Activity activity) {
        assertNotNull(activity);
        assertNotNull(activity.at);
        assertTrue(StringUtils.isNotBlank(activity.getMessage(LinkActivityTest.DEFAULT_LANG)));
    }
}
