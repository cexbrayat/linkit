package controllers;

import java.util.List;
import java.util.Set;
import models.Article;
import models.Member;
import models.ProviderType;
import models.Session;
import models.activity.Activity;
import play.data.binding.As;
import play.db.jpa.Transactional;
import play.mvc.Controller;

/**
 * Controller for Ajax loading of activities
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Transactional(readOnly = true)
public class Activities extends Controller {

    public static final String PROVIDERS_SEP = "~";

    /**
     * General activities
     * @param page
     * @param size 
     */
    public static void general(Integer page, Integer size) {
        List<Activity> _activities = Activity.recents(page, size);
        render("tags/activities.html", _activities);
    }

    public static void of(String login, @As(PROVIDERS_SEP) Set<ProviderType> providers, Integer page, Integer size) {
        Member member = Member.findByLogin(login);
        List<Activity> _activities = Activity.recentsByMember(member, providers, page, size);
        render("tags/activities.html", _activities);
    }

    public static void incoming(@As(PROVIDERS_SEP) Set<ProviderType> providers, Integer page, Integer size) {
        Member member = Member.findByLogin(Security.connected());
        List<Activity> _activities = Activity.recentsForMember(member, providers, page, size);
        render("tags/activities.html", _activities);
    }

    public static void session(long sessionId, Integer page, Integer size) {
        Session talk = Session.findById(sessionId);
        List<Activity> _activities = Activity.recentsBySession(talk, page, size);
        render("tags/activities.html", _activities);
    }

    public static void article(long articleId, Integer page, Integer size) {
        Article article = Article.findById(articleId);
        List<Activity> _activities = Activity.recentsByArticle(article, page, size);
        render("tags/activities.html", _activities);
    }
}
