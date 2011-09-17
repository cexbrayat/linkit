import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

	@Before
	public void setUp() {
    	Fixtures.deleteAll();
	    Fixtures.load("data.yml");
	}

    @Test
    public void connect() {
		assertEquals(false, Member.connect("bob", "bob"));
		assertEquals(false, Member.connect("ced", "bob"));
		assertEquals(true, Member.connect("bob", "secret"));
    }

}
