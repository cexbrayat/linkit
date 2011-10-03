package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import models.activity.Activity;

public class Application extends Controller {

    public static void index() {
        List<Map> tags = Interest.getCloud();
        List<Activity> activities = Activity.recents(10);
        render(tags,activities);
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
}