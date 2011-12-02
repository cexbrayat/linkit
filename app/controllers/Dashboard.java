package controllers;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import models.Article;
import models.Badge;
import models.Comment;
import models.Member;
import models.Session;
import models.Suggestion;

public class Dashboard extends PageController {

    public static void index() {
        Member member = Member.fetchForProfile(Security.connected());

        Set<Member> suggestedMembers = Suggestion.suggestedMembersFor(member);
        Set<Session> suggestedSessions = Suggestion.suggestedSessionsFor(member);
        Set<Badge> suggestedBadges = Suggestion.missingBadgesFor(member);

        // Three recent articles
        List<Article> articles = Article.recents(1, 3);

        // Five recent comments
        List<Comment> comments = Comment.recentsByMember(member, 5);

        render(member, suggestedMembers, suggestedSessions, suggestedBadges, articles, comments);
    }

    public static void link(String loginToLink) {
        Member.addLink(Security.connected(), loginToLink);
        flash.success("Link ajouté!");
        index();
    }
}