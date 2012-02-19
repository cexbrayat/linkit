package models;

import org.junit.*;

/**
 * Unit tests for {@link Setting} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class SettingTest extends BaseDataUnitTest {
   
    protected static Member createMember(final String login) {
        return new Member(login).save();
    }

    @Test public void findNotified() {
        final Member m = createMember("toto");
        Setting s = new Setting(m);
        s.notificationOption = NotificationOption.Daily;
        s.save();

        assertTrue(Setting.findNotified(NotificationOption.Daily).contains(m));
        assertFalse(Setting.findNotified(NotificationOption.Instant).contains(m));
        assertFalse(Setting.findNotified(NotificationOption.None).contains(m));
    }

    @Test public void findByMember() {
        final Member m = createMember("toto");
        final Setting s = new Setting(m).save();

        assertSame(s, Setting.findByMember(m));
        assertNull(null, Setting.findByMember(createMember("other")));
    }
}
