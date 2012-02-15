package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import models.validation.TimeZoneCheck;
import play.data.validation.CheckWith;
import play.db.jpa.Model;

/**
 * Settings for a {@link Member}
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class Setting extends Model {
    
    @OneToOne(optional = false)
    public Member member;
    
    @Enumerated(EnumType.STRING)
    public NotificationOption notificationOption = NotificationOption.Weekly;

    /** Timezone for activities hours display (in mail notification) */
    @CheckWith(TimeZoneCheck.class)
    public String timezone = "Europe/Paris";

    public Setting(Member member) {
        this.member = member;
    }
        
    public static List<Member> findNotified(NotificationOption option) {
        return find("select s.member from Setting s where s.notificationOption=?", option).fetch();
    }

    public static Setting findByMember(final Member member) {
        return find("from Setting s where s.member=?", member).first();
    }
}
