package controllers;

import java.util.List;
import java.util.Set;

import models.Article;
import models.Comment;
import models.Member;
import models.Suggestion;

public class Dashboard extends PageController {

    public static void index() {
        Member member = Member.fetchForProfile(Security.connected());
        Set<Member> suggests = Suggestion.suggestedMembersFor(member);

        // Three recent articles
        List<Article> articles = Article.recents(1, 3);

        List<Comment> comments = Comment.recentsByMember(member, 5);

        render(member, suggests, articles, comments);
    }

    public static void link(String loginToLink) {
        Member.addLink(Security.connected(), loginToLink);
        flash.success("Link ajouté!");
        index();
    }
}