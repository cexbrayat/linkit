package models.mailing;

import com.google.common.collect.Sets;
import models.BaseDataUnitTest;
import models.Member;
import models.Talk;
import org.junit.Test;
import play.test.Fixtures;

/**
 * Unit tests for {@link MembersSetQuerySpeakers}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class MembersSetQueryFailedSpeakersSet extends BaseDataUnitTest {
    
    private MembersSetQuery query = MembersSetQueryFactory.create(MembersSet.FailedSpeakers);
    
    private static Member createMember(final String login){
        Member m = new Member(login);
        m.firstname = login;
        m.lastname = login;
        return m.save();
    }
    
    private static Talk createTalk(final String title, Member speaker, boolean valid){
        Talk t = new Talk();
        t.title = title;
        t.addSpeaker(speaker);
        t.save();
        if (valid) {
            t.validate();
        }
        return t;
    }
    
    @Test public void find() {

        Fixtures.deleteAllModels();

        final Member speaker1 = createMember("speaker1");
        createTalk("test1 valid", speaker1, true);
        createTalk("test1 non valid", speaker1, false);

        final Member speaker2 = createMember("speaker2");
        createTalk("test2 valid", speaker2, true);

        final Member speaker3 = createMember("speaker3");
        createTalk("test3 non valid", speaker3, false);

        final Member member1 = createMember("member1");

        assertEquals(Sets.newHashSet(speaker3), Sets.newHashSet(query.find()));
    }
}
