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
}