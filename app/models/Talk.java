package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import models.activity.NewTalkActivity;
import models.activity.UpdateSessionActivity;
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
    
    public boolean valid;

    public static List<Talk> recents(int page, int length) {
        return find("order by addedAt desc").fetch(page, length);
    }
    
    public static long countSpeakers() {
        return find("select count(distinct s) from Talk t inner join t.speakers as s where t.valid=true").first();
    }
    
    public static List<Member> findAllSpeakers() {
        return find("select distinct t.speakers from Talk t where t.valid=true").fetch();
    }
    
    public static long countTalksByMember(Member member) {
        return find("select count(distinct t) from Talk t inner join t.speakers as s where t.valid=true and ? = s", member).first();
    }
    
    public static List<Talk> findAllValidated() {
        return find("valid=true").fetch();
    }

    @Override
    public void update() {
        save();
        // On ne déclenche une activité publique de mise à jour que si la session est valide (donc visible publiquement)
        if (valid) {
            new UpdateSessionActivity(this).save();
        }
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
    public String getShowUrl() {
        return Router
                .reverse("Sessions.show")
                .add("sessionId", this.id)
                .url;
    }
}
