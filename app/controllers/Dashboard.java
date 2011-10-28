package controllers;

import java.util.Set;
import models.Member;
import models.Suggestion;
import play.mvc.*;

public class Dashboard extends Controller {

    public static void index() {
        Member member = Member.fetchForProfile(Security.connected());
        Set<Member> suggests = Suggestion.suggestedMembersFor(member);
        render(member, suggests);
    }

    public static void link(String loginToLink) {
        Member.addLink(Security.connected(), loginToLink);
        flash.success("Link ajouté!");
        index();
    }
}