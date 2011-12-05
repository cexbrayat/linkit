package models.auth;

import models.BaseDataUnitTest;
import models.Member;
import models.ProviderType;
import org.junit.*;

/**
 * Unit tests for {@link AuthAccount} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class AuthAccountTest extends BaseDataUnitTest {

    @Test
    public void find() {
        final String login = "ced";
        assertNotNull(AuthAccount.find(ProviderType.LinkIt, login));
    }

    @Test
    public void deleteForMember() {
        final Member member = Member.all().first();
        assertNotNull(AuthAccount.deleteForMember(member));
    }
}
