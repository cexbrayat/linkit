package models;


import org.junit.*;
import play.test.*;

/**
 * Unit tests for {@link Comment} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class CommentTest extends UnitTest {

    @Before
    public void setUp() {
        Fixtures.deleteAllModels();
        Fixtures.loadModels("data.yml");
    }

    @After
    public void tearDown() {
        Fixtures.deleteAllModels();
    }

    @Test
    public void testCountByMember() {
        Member member = Member.all().first();
        final long count = Comment.countByMember(member);
        assertTrue(count >= 0);
    }

    @Test
    public void deleteForMember() {
        Member member = Member.all().first();
        Comment.deleteForMember(member);
    }
}
