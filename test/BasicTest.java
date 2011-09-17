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
    
    @Test
    public void addLink() {
        Member bob = Member.find("byLogin","bob").first();
        assertEquals(0, bob.links.size());
        Member.addLink("bob","ced");
        assertEquals(1, bob.links.size());
    }

}
