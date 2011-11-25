package models;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.persistence.Entity;
import models.activity.StatusActivity;
import play.mvc.Router;

/**
 * An account on Link-IT (basic login/password local authentication)
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class LinkItAccount extends Account {

    public String password;

    public LinkItAccount(String password) {
        super(ProviderType.LinkIt);
        this.password = password;
    }
    
    @Override
    public String toString(){
        return "Link-IT account for " + member;
    }

    @Override
    public List<StatusActivity> fetchActivities() {
        // No activities to fetch;
        return Collections.emptyList();
    }

    @Override
    public void enhance(Collection<StatusActivity> activities) {
        // Useless;
    }

    @Override
    public String getUrl() {
        return Router.reverse("Profile.show").add("login", member.login).url;
    }
}
