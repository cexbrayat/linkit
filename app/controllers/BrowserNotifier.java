package controllers;

import com.google.gson.Gson;
import models.Member;
import models.activity.Activity;
import play.Logger;
import play.libs.WS;
import play.templates.Template;
import play.templates.TemplateLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: cedric
 * Date: 25/02/12
 * Time: 18:40
 * To change this template use File | Settings | File Templates.
 */
public class BrowserNotifier {

    static Gson gson = new Gson();

    public static void send(Activity activity) {
        if (activity != null) {
            Logger.info("activity send " + activity);

            //render activity
            String html = renderActivity(activity);

            //user interested
            StringBuffer users = getUsersInterested(activity.member.linkers);

            //send
            WS.url("http://localhost:9002/").body("{ \"content\":\"" + html + "\", \"users\":" + users.toString() + "}").postAsync();
        }
    }

    private static String renderActivity(Activity activity) {
        Template template = TemplateLoader.load("tags/activity.html");
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("_arg", activity);
        String html = template.render(args);
        //escape quotes
        html = html.replace("\"", "\\\"");
        //escape return
        html = html.replace("\n", "");
        return html;
    }

    private static StringBuffer getUsersInterested(Set<Member> members) {
        //users
        StringBuffer users = new StringBuffer();
        users.append("[");
        if (!members.isEmpty()) {
            for (Member member : members) {
                users.append("\"");
                users.append(member.login);
                users.append("\",");
            }
            users.deleteCharAt(users.length() - 1);
        }
        users.append("]");
        return users;
    }
}
