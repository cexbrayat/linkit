package models;

import models.dto.SpeakerPreferencesDTO;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: mpetitdant
 * Date: 11/02/15
 * Time: 01:39
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table( uniqueConstraints = {
        @UniqueConstraint(name = "SpeakerEventUnique", columnNames = {"speaker_id", "event"})
})
public class SpeakerPreferences extends Model {


    @Enumerated(EnumType.STRING)
    @Required
    public ConferenceEvent event;

    @Required
    @ManyToOne(optional = false)
    public Member speaker;

    @Column
    @Lob
    public String additionalDetails;

    @Column
    @Enumerated(EnumType.STRING)
    public TransportationType transportationType;

    @Column
    public String arrivalTime;

    @Column
    public String arrivalPlace;

    @Column
    public String departureTime;

    @Column
    public String departurePlace;

    @Column
    public boolean eveningBefore;

    @Column
    public boolean eveningDuring;

    @Column
    public boolean eveningAfter;

    @Column
    public boolean hotelNightBefore;

    @Column
    public boolean hotelNightDuring;

    @Column
    public boolean hotelNightAfter;

    @Column
    @Lob
    public String additionalHotelDetails;

    @Column
    public boolean presenceFirstAM;
    @Column
    public boolean presenceFirstPM;
    @Column
    public boolean presenceSecondAM;
    @Column
    public boolean presenceSecondPM;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date lastEditTime;


    public void updateFromDTO(SpeakerPreferencesDTO preferences) {
        additionalDetails = preferences.additionalDetails;
        transportationType = preferences.transportationType;
        arrivalTime = preferences.arrivalTime;
        arrivalPlace = preferences.arrivalPlace;
        departureTime = preferences.departureTime;
        departurePlace = preferences.departurePlace;
        eveningBefore = preferences.eveningBefore;
        eveningDuring = preferences.eveningDuring;
        eveningAfter = preferences.eveningAfter;
        hotelNightBefore = preferences.hotelNightBefore;
        hotelNightDuring = preferences.hotelNightDuring;
        hotelNightAfter = preferences.hotelNightAfter;
        additionalHotelDetails = preferences.additionalHotelDetails;
        presenceFirstAM = preferences.presenceFirstAM;
        presenceFirstPM = preferences.presenceFirstPM;
        presenceSecondAM = preferences.presenceSecondAM;
        presenceSecondPM = preferences.presenceSecondPM;

    }

    @PrePersist
    public void updateLastEditTime() {
        lastEditTime = new Date();
    }

    public String getArrivalTimeStr(String dateFormat) {
        final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(dateFormat);
        return SIMPLE_DATE_FORMAT.format(arrivalTime);
    }

    public String getDepartureTimeStr(String dateFormat) {
        final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(dateFormat);
        return SIMPLE_DATE_FORMAT.format(departureTime);
    }
}
