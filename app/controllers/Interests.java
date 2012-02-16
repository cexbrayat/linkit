package controllers;

import java.util.ArrayList;
import java.util.List;
import models.Interest;
import models.Role;
import play.data.binding.As;
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

    public static void delete(Long[] interestsToBeDeleted) {
        if (interestsToBeDeleted != null) {
            for (Long interestId : interestsToBeDeleted) {
                Interest i = Interest.findById(interestId);
                i.delete();
            }
            flash.success("Intérêt(s) supprimé(s)");
        }
        edit();
    }

    public static void chooseInterestForMerge(Long[] interestsToBeDeleted) {
        List<Interest> interests = new ArrayList<Interest>(interestsToBeDeleted.length);
        for (Long interestId : interestsToBeDeleted) {
            Interest i = Interest.findById(interestId);
            interests.add(i);
        }
        render("Interests/merge.html", interests);
    }

    public static void merge(@As(",") Long[] interests, Long survivorInterestId) {
        Interest survivorInterest = Interest.findById(survivorInterestId);
        if (interests != null) {
            for (Long interestToBeDeleted : interests) {
                Interest i = Interest.findById(interestToBeDeleted);
                i.merge(survivorInterest);
            }
            flash.success("Intérêts fusionnés");
        }
        edit();
    }

    public static void rename(Long interestId) {
        Interest interest = Interest.findById(interestId);
        render(interest);
    }
    
    public static void submitRename(Long interestId,String newNameInterest) {
        Interest interest = Interest.findById(interestId);
        interest.name = newNameInterest;
        interest.save();
        flash.success("l'intérêt a été renommé en '%s'", newNameInterest);
        edit();
    }

}