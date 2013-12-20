package models;

import com.google.common.base.Predicate;
import controllers.Application;

import java.io.Serializable;

public abstract class SessionPredicates implements Serializable {
    public static final Predicate<Session> CURRENT_VALIDATED_TALK = new TalkPredicate(true, ConferenceEvent.CURRENT);
    public static final Predicate<Session> PREVIOUS_VALIDATED_TALK = new PreviousValidatedTalkPredicate(ConferenceEvent.CURRENT);
    public static final Predicate<Session> CURRENT_PROPOSED_TALK = new TalkPredicate(false, ConferenceEvent.CURRENT);
    public static final Predicate<Session> CURRENT_LIGHTNING_TALK = new LightningTalkPredicate(ConferenceEvent.CURRENT);

    static class TalkPredicate implements Predicate<Session> {

        private boolean validated;
        private ConferenceEvent event;

        public TalkPredicate(boolean validated, ConferenceEvent event) {
            this.validated = validated;
            this.event = event;
        }

        public boolean apply(Session s) {
            if (s instanceof Talk) {
                Talk t = (Talk) s;
                return t.valid == this.validated && t.event == this.event;
            } else {
                return false;
            }
        }

    }

    static class PreviousValidatedTalkPredicate implements Predicate<Session> {

        private ConferenceEvent exclusiveMaxEvent;

        public PreviousValidatedTalkPredicate(ConferenceEvent exclusiveMaxEvent) {
            this.exclusiveMaxEvent = exclusiveMaxEvent;
        }

        public boolean apply(Session s) {
            if (s instanceof Talk) {
                Talk t = (Talk) s;
                return t.valid == true && t.event.compareTo(this.exclusiveMaxEvent) < 0;
            } else {
                return false;
            }
        }

    }

    static class LightningTalkPredicate implements Predicate<Session> {

        private ConferenceEvent event;

        public LightningTalkPredicate(ConferenceEvent event) {
            this.event = event;
        }

        public boolean apply(Session s) {
            if (s instanceof LightningTalk) {
                return s.event == this.event;
            } else {
                return false;
            }
        }

    }
}