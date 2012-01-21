package controllers;

import java.util.List;
import java.util.Set;

import models.Article;
import models.Badge;
import models.Comment;
import models.Member;
import models.Session;
import models.Suggestion;
import play.Logger;

public class Dashboard extends PageController {

    public static void index() {
// FIXME CLA Member.fetchForProfile
//      Member member = Member.fetchForProfile(login);
        Member member = Member.findByLogin(Security.connected());

        Set<Member> suggestedMembers = Suggestion.suggestedMembersFor(member);
        Set<Session> suggestedSessions = Suggestion.suggestedSessionsFor(member);
        Set<Badge> suggestedBadges = Suggestion.missingBadgesFor(member);

        // Three recent articles
        List<Article> articles = Article.recents(1, 3);

        // Five recent comments
        List<Comment> comments = Comment.recentsByMember(member, 5);

        render(member, suggestedMembers, suggestedSessions, suggestedBadges, articles, comments);
    }
    
    public static void badges() {
        Member member = Member.findByLogin(Security.connected());
        Set<Badge> suggestedBadges = Suggestion.missingBadgesFor(member);
        render(member, suggestedBadges);
    }

    public static void link(String loginToLink) {
        Member.addLink(Security.connected(), loginToLink);
        flash.success("Link ajouté!");
        index();
    }
    
    public static void delete() throws Throwable {
        Member member = Member.findByLogin(Security.connected());
        render(member);
    }
    
    public static void confirmDelete() throws Throwable {
        Member member = Member.findByLogin(Security.connected());
        member.delete();
        Logger.info("Deleted profile %s", member);
        flash.success("Votre compte a été supprimé");
        Secure.logout();
    }
}