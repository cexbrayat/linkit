package models.mailing;

import com.google.common.collect.Sets;
import models.BaseDataUnitTest;
import models.ConferenceEvent;
import models.LightningTalk;
import models.Member;
import org.junit.Test;

import java.util.List;

public class MembersSetQueryLTSpeakersTest extends BaseDataUnitTest {

    private MembersSetQuery query = MembersSetQueryFactory.create(MembersSet.LTSpeakers);

    private LightningTalk createLT(ConferenceEvent event, String title, Member... speakers) {
        LightningTalk lt = new LightningTalk();
        lt.title = title;
        lt.event = event;

        if (speakers != null) {
            for (Member speaker : speakers) {
                lt.addSpeaker(speaker);
            }
        }

        return lt.save();
    }

    private static Member createMember(final String login){
        Member m = new Member(login);
        m.firstname = login;
        m.lastname = login;
        return m.save();
    }

    @Test public void find() {

        Member speaker1 = createMember("speaker1");
        Member speaker2 = createMember("speaker2");
        Member speaker3 = createMember("speaker3");
        Member notSpeaker = createMember("speaker4");
        createLT(ConferenceEvent.CURRENT, "title1", speaker1, speaker2);
        createLT(ConferenceEvent.CURRENT, "title2", speaker1, speaker3);
        createLT(ConferenceEvent.mixit12, "title3", speaker1, speaker2, notSpeaker);
        List<? extends Member> speakers = query.find();
        assertEquals(LightningTalk.findAllSpeakersOn(ConferenceEvent.CURRENT), speakers);
        assertTrue(speakers.containsAll(Sets.newHashSet(speaker1, speaker2, speaker3)));
        assertFalse(speakers.contains(notSpeaker));
    }
}
