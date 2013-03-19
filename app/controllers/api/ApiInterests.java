package controllers.api;

import models.Interest;
import play.mvc.Controller;

import java.util.List;

public class ApiInterests extends JsonpController {

    public static void list() {
        List<Interest> interests = Interest.findAllOrdered();
        renderJSON(interests);
    }

    public static void interest(long id) {
        Interest interest = Interest.findById(id);
        notFoundIfNull(interest);
        renderJSON(interest);
    }
}
