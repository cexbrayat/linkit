package controllers;

import java.util.List;
import java.util.Set;

import models.Article;
import models.Badge;
import models.Comment;
import models.Member;
import models.NotificationOption;
import models.Session;
import models.Suggestion;
import play.data.validation.Required;
import play.mvc.With;

@With(SecureLinkIt.class)
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
    
    public static void settings() {
        Member member = Member.findByLogin(Security.connected());
        if (member == null) Login.index(request.url);
        
        render(member);
    }
    
    public static void saveSettings(@Required NotificationOption notificationOption) {
        Member member = Member.findByLogin(Security.connected());
        if (member == null) Login.index(request.url);
        
        member.notificationOption = notificationOption;
        
        member.save();
        flash.success("Vos préférences ont été sauvegardées");

        index();
    }

    public static void link(String loginToLink) {
        Member.addLink(Security.connected(), loginToLink);
        flash.success("Link ajouté!");
        index();
    }
}