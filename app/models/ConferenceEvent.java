package models;

public enum ConferenceEvent {

    // never modify this existing values heavily used in DB data.
    // Their order should also be kept chronological (used to sort sessions).
    mixit12,
    mixit13,
    mixit14;
    // but you're obviously welcome to add new event instances, my friend.

    public boolean isCurrent() {
        return CURRENT == this;
    }

    public static final ConferenceEvent CURRENT = ConferenceEvent.mixit14;

}
