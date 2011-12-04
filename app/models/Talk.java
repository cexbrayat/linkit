package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import play.data.validation.Required;

/**
 * A talk session
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class Talk extends Session {

    @Required
    @Enumerated(EnumType.STRING)
    public Track track;

    public static List<Talk> recents(int page, int length) {
        return find("order by addedAt desc").fetch(page, length);
    }
}
