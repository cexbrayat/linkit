package controllers;

import models.Interest;
import models.Member;
import models.Session;
import models.Suggestion;
import models.serialization.*;
import play.Logger;
import play.mvc.With;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@With(Secure.class)
public class Suggestions extends PageController {

   public static void sessionsByInterests(Long[] interestsIds){
       Logger.info("ids: " + interestsIds.length);

       notFoundIfNull(interestsIds);

       Collection<Interest> interests = new ArrayList<Interest>();
       for (int i = 0; i < interestsIds.length; i++) {
           Long id = interestsIds[i];
           Interest interest = Interest.findById(id);
           if(interest != null){
               interests.add(interest);
           }
       }
       Logger.info("Interests: " + interests);
       Collection<Session> sessions = Suggestion.findSessionsAbout(interests);
       renderJSON(sessions, new TalkIdSerializer(), new LightningTalkIdSerializer());

   }
    
   public static void membersByInterests(Long[] interestsIds){
       Logger.info("ids: " + interestsIds);
       Collection<Interest> interests = new ArrayList<Interest>();
       for (int i = 0; i < interestsIds.length; i++) {
           Long id = interestsIds[i];
           Interest interest = Interest.findById(id);
           if(interest != null){
               interests.add(interest);
           }
       }
       Logger.info("Interests: " + interests);
       List<Member> membersInterested = Suggestion.findMembersInterestedInAllOf(interests);
       renderJSON(membersInterested, new MemberIdSerializer(), new SponsorIdSerializer(), new StaffIdSerializer());
   }
}