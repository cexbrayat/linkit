package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import models.activity.Activity;

public class Application extends Controller {

    public static void index() {
        
        // DEV MODE : discard connected user if not found in DB (when you restarted your local dev application with initial data)
        String login = Security.connected();
        if (login != null && Member.findByLogin(login) == null) {
            session.remove("username");
        }
        List<Map> tags = Interest.getCloud();
        render(tags);
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

    public static void membersByInterest(String interest) {
        List<Member> members = Member.findMembersInterestedBy(interest);
        Logger.info(Member.count() + " membres interested by " + interest);
        render("Application/list.html", members, interest);
    }
    
    public static void activities(Integer page, Integer size) {
        List<Activity> _activities = Activity.recents(page, size);
        render("tags/activities.html", _activities);
    }
}