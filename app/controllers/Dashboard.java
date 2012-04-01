package controllers;

import java.util.List;
import java.util.Set;

import models.Article;
import models.Badge;
import models.Comment;
import models.Member;
import models.Session;
import models.Setting;
import models.Suggestion;
import org.joda.time.DateTimeZone;
import play.data.validation.Valid;
import play.mvc.With;

@With(SecureLinkIt.class)
public class Dashboard extends PageController {

    public static void index() {
// FIXME CLA Member.fetchForProfile
//      Member member = Member.fetchForProfile(login);
        Member member = Member.findByLogin(Security.connected());
        Setting setting = Setting.findByMember(member);

        List<Member> suggestedMembers = Suggestion.suggestedMembersFor(member, 5);
        List<Session> suggestedSessions = Suggestion.suggestedSessionsFor(member, 5);
        Set<Badge> suggestedBadges = Suggestion.missingBadgesFor(member);

        // Three recent articles
        List<Article> articles = Article.recents(1, 3);

        // Five recent comments
        List<Comment> comments = Comment.recentsByMember(member, 5);

        render(member, setting, suggestedMembers, suggestedSessions, suggestedBadges, articles, comments);
    }
    
    public static void badges() {
        Member member = Member.findByLogin(Security.connected());
        Set<Badge> suggestedBadges = Suggestion.missingBadgesFor(member);
        render(member, suggestedBadges);
    }
    
    public static void settings() {
        Member member = Member.findByLogin(Security.connected());
        if (member == null) Login.index(request.url);
        
        Setting setting = Setting.findByMember(member);
        if (setting == null) {
            setting = new Setting(member);
        }
        settings(setting);
    }
    
    private static void settings(Setting setting) {
        Set<String> timezones = DateTimeZone.getAvailableIDs();     
        render("Dashboard/settings.html", setting, timezones);
    }
    
    public static void saveSettings(@Valid Setting setting) {
        Member member = Member.findByLogin(Security.connected());
        if (member == null) Login.index(request.url);

        if (validation.hasErrors()) {
            settings(setting);
        }
        setting.member = member;
        setting.save();
        flash.success("Vos préférences ont été sauvegardées");

        index();
    }

    public static void link(String loginToLink) {
        Member.addLink(Security.connected(), loginToLink);
        flash.success("Link ajouté!");
        index();
    }
}