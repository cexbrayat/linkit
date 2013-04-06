package models.mailing;

import java.util.List;

import models.ConferenceEvent;
import models.Member;
import models.Talk;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class MembersSetQueryFailedSpeakers implements MembersSetQuery {

    public List<Member> find() {
        return Talk.findFailedSpeakersOn(ConferenceEvent.CURRENT);
    }
}
