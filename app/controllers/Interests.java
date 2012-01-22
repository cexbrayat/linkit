package controllers;

import models.Interest;

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
        }
        render("Interests/edit.html");
    }

    public static void chooseInterestForMerge(String[] interestsToBeDeleted) {
        render("Interests/merge.html", interestsToBeDeleted);
    }

    public static void merge(String[] interestsToBeDeleted, String survivorInterestName) {
        if (interestsToBeDeleted != null) {
            for (String interestNameToBeDeleted : interestsToBeDeleted) {
                Interest interestToBeDeleted = Interest.findByName(interestNameToBeDeleted);
                interestToBeDeleted.merge(survivorInterestName);
            }
        }
        render("Interests/edit.html");
    }
}