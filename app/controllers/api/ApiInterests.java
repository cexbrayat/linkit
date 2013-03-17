package controllers.api;

import models.Interest;
import play.mvc.Controller;

import java.util.List;

public class ApiInterests extends Controller {

    public static void list() {
        List<Interest> interests = Interest.findAllOrdered();
        renderJSON(interests);
    }
}
