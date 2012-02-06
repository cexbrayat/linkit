package controllers;

import models.Interest;
import models.Role;
import play.mvc.With;

@With(SecureLinkIt.class)
@Check(Role.ADMIN_INTEREST)
public class Interests extends PageController {

    public static void edit() {
        render("Interests/edit.html");
    }

    public static void moderate() {
        render("Interests/edit.html");
    }

    public static void delete(String[] interestsToBeDeleted) {
        if (interestsToBeDeleted != null) {
            Interest.deleteByName(interestsToBeDeleted);
            flash.success("Intérêt(s) supprimé(s)");
        }
        render("Interests/edit.html");
    }

    public static void chooseInterestForMerge(String[] interestsToBeDeleted) {
        render("Interests/merge.html", interestsToBeDeleted);
    }

    public static void merge(String[] interestsToBeDeleted, String survivorInterestName) {

        Interest survivorInterest = Interest.findByName(survivorInterestName);
        if (interestsToBeDeleted != null) {
            for (String interestNameToBeDeleted : interestsToBeDeleted) {
                Interest interestToBeDeleted = Interest.findByName(interestNameToBeDeleted);
                interestToBeDeleted.merge(survivorInterest);
            }
            flash.success("Intérêts fusionnés");
        }
        render("Interests/edit.html");
    }
}