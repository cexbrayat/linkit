package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import play.data.validation.Required;
import play.modules.search.Indexed;

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
    
    public void validate() {
        this.valid = true;
        save();
    }
    
    public void unvalidate() {
        this.valid = false;
        save();
    }
}
