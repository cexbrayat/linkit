package models.mailing;

import models.ConferenceEvent;
import models.LightningTalk;
import models.Member;

import java.util.List;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class MembersSetQueryLTSpeakers implements MembersSetQuery {

    public List<Member> find() {
        return LightningTalk.findAllSpeakersOn(ConferenceEvent.CURRENT);
    }
}
