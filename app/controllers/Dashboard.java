package controllers;

import java.util.List;
import java.util.Set;
import models.Article;
import models.Member;
import models.Suggestion;
import play.mvc.*;

public class Dashboard extends Controller {

    public static void index() {
        Member member = Member.fetchForProfile(Security.connected());
        Set<Member> suggests = Suggestion.suggestedMembersFor(member);

        // Three recent articles
        List<Article> articles = Article.recents(1, 3);

        render(member, suggests, articles);
    }

    public static void link(String loginToLink) {
        Member.addLink(Security.connected(), loginToLink);
        flash.success("Link ajouté!");
        index();
    }
}