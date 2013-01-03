package models;

public enum ConferenceEvent {
    mixit12,
    mixit13;

    public boolean isCurrent() {
        return CURRENT == this;
    }

    public static final ConferenceEvent CURRENT = ConferenceEvent.mixit13;

}
