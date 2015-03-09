package models.dto;

import models.TransportationType;

/**
 * @author mpetitdant
 *         Date: 08/03/15
 */

public class SpeakerPreferencesDTO {

    public Integer id;
    public String additionalDetails;
    public TransportationType transportationType;
    public String arrivalTime;
    public String arrivalPlace;
    public String departureTime;
    public String departurePlace;
    public boolean pickup;
    public boolean eveningBefore;
    public boolean eveningDuring;
    public boolean eveningAfter;
    public boolean hotelNightBefore;
    public boolean hotelNightDuring;
    public boolean hotelNightAfter;
    public String additionalHotelDetails;
    public boolean presenceFirstAM;
    public boolean presenceFirstPM;
    public boolean presenceSecondAM;
    public boolean presenceSecondPM;
}
