package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }


    public static void showMembers() {
        List<Member> members = Member.findAll();
        Logger.info(Member.count() + " membres");
        render("Application/list.html", members);
    }
    
    public static void showMembersbyInterest(String interest) {
        List<Member> members = Member.findMembersInterestedBy(interest);
        Logger.info(Member.count() + " membres interested by "+interest);
        render("Application/list.html", members,interest);
    }


}