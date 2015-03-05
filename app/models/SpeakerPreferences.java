package models;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import controllers.Application;
import controllers.Mails;
import java.util.*;
import javax.annotation.Nullable;
import javax.persistence.*;
import models.activity.Activity;
import models.activity.CommentSessionActivity;
import models.activity.LookSessionActivity;
import models.activity.UpdateSessionActivity;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.joda.time.LocalDateTime;
import play.Logger;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.modules.search.Field;
import play.modules.search.Field;

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
    @ManyToOne(optional = false)
    public ConferenceEvent event;

    @OneToOne
    @Required
    public Member speaker;

    @Column
    @Lob
    @Field
    public String additionalDetails;

    @Column
    @Enumerated(EnumType.STRING)
    public TransportationType transportationType;

    @Column
    public Date arrivalTime;

    @Column
    public String arrivalPlace;

    @Column
    public Date departureTime;

    @Column
    public String departurePlace;

    @Column
    public boolean pickup;

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
    public String additionalHotelDetails;

    @Column
    public boolean presenceFirstAM;
    @Column
    public boolean presenceFirstPM;
    @Column
    public boolean presenceSecondAM;
    @Column
    public boolean presenceSecondPM;



}
