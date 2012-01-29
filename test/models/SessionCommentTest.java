package models;

import org.junit.*;

/**
 * Unit tests for {@link SessionComment} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class SessionCommentTest extends BaseDataUnitTest {

    @Test
    public void deleteForSession() {
        Session session = Session.all().first();
        assertNotNull(SessionComment.deleteForSession(session));
    }
}
