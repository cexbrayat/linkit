package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import models.activity.NewTalkActivity;
import play.data.validation.Required;
import play.modules.search.Indexed;
import play.mvc.Router;

/**
 * A talk session
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
@Indexed
public class Talk extends Session {

    @Required
    @Enumerated(EnumType.STRING)
    public Track track;

    public static List<Talk> recents(int page, int length) {
        return find("valid=true order by addedAt desc").fetch(page, length);
    }
    
    public static long countSpeakers() {
        return find("select count(distinct s) from Talk t inner join t.speakers as s where t.valid=true").first();
    }
    
    public static List<Member> findAllSpeakers() {
        return find("select distinct t.speakers from Talk t where t.valid=true").fetch();
    }
    
    public static List<Talk> findAllValidated() {
        return find("valid=true").fetch();
    }
    
    public void validate() {
        this.valid = true;
        save();
        new NewTalkActivity(this).save();
    }
    
    public void unvalidate() {
        this.valid = false;
        save();
    }

    @Override
    public void lookedBy(Member member) {
        if (valid) {
            super.lookedBy(member);
        }
        // Un talk non validé n'est pas consultable par le public : on ne compte pas les visites, ni ne génère d'activité
    }

    @Override
    public String getShowUrl() {
        return Router
                .reverse("Sessions.show")
                .add("sessionId", this.id)
                .url;
    }
}
