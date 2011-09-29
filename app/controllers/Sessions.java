package controllers;

import play.*;
import play.mvc.*;

import java.util.*;
import models.Session;

public class Sessions extends Controller {

    public static void index() {
        List<Session> sessions = Session.findAll();
        Logger.info(sessions.size() + " sessions");
        render("Sessions/list.html", sessions);
    }

}