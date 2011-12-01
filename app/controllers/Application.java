package controllers;

import models.*;
import play.Logger;

import java.util.List;
import java.util.Map;

public class Application extends PageController {
    
    public static void index() {
        
        // DEV MODE : discard connected user if not found in DB (when you restarted your local dev application with initial data)
        String login = Security.connected();
        if (login != null && Member.findByLogin(login) == null) {
            session.remove("username");
        }
        
        // Three recent articles
        List<Article> articles = Article.recents(1, 3);
        List<Session> sessions = Session.recents(1, 3);
        List<Member> members = Member.recents(1, 6);
        List<Map> tags = Interest.getCloud();
        render(articles, sessions, members, tags);
    }

    public static void members() {
        List<Member> members = Member.findAll();
        Logger.info(members.size() + " membres");
        render("Application/list.html", members);
    }

    public static void staff() {
        List<Staff> members = Staff.findAll();
        Logger.info(members.size() + " membres du staff");
        render("Application/list.html", members);
    }

    public static void speakers() {
        List<Speaker> members = Speaker.findAll();
        Logger.info(members.size() + " speakers");
        render("Application/list.html", members);
    }

    public static void sponsors() {
        List<Sponsor> members = Sponsor.findAll();
        Logger.info(members.size() + " sponsors");
        render("Application/list.html", members);
    }

    public static void sessionsAndMembersByInterest(String interest) {
        List<Member> members = Member.findMembersInterestedIn(interest);
        Logger.info(Member.count() + " membres interested by " + interest);
        List<Session> sessions = Session.findSessionsLinkedWith(interest);
        Logger.info(Session.count() + " session linked with " + interest);
        render("Interests/list.html", members, sessions, interest);
    }
}