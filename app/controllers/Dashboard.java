package controllers;

import java.util.List;
import java.util.Set;

import models.LightningTalk;
import models.Article;
import models.Member;
import models.Suggestion;
import play.mvc.*;

public class Dashboard extends PageController {

    public static void index() {
        Member member = Member.fetchForProfile(Security.connected());
        Set<Member> suggests = Suggestion.suggestedMembersFor(member);

        // Three recent articles
        List<Article> articles = Article.recents(1, 3);

        // TODO CLA Afficher nos commentaires
        
        List<LightningTalk> lightningTalks = LightningTalk.findByMember(member);
        render(member, suggests, articles, lightningTalks);
    }

    public static void link(String loginToLink) {
        Member.addLink(Security.connected(), loginToLink);
        flash.success("Link ajouté!");
        index();
    }
}