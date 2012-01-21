package models.generator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import models.Article;
import models.ArticleComment;
import models.Badge;
import models.GoogleAccount;
import models.Interest;
import models.Member;
import models.Session;
import models.SessionComment;
import models.SharedLink;
import models.TwitterAccount;
import play.Logger;

/**
 * Creates dummy data for load tests.
 * @author Sryl <cyril.lacote@gmail.com>
 */
public final class DataGenerator {
    
    private static AtomicInteger nbMembers = new AtomicInteger(0);
    protected static Member createMember() {
        Member m = new Member("login"+nbMembers.getAndIncrement());
        
        m.company = Dummy.randomString(30);
        m.email = Dummy.randomEmail();
        m.firstname = Dummy.randomName(20);
        m.lastname = Dummy.randomName(30);
        m.longDescription = Dummy.randomText(5000);
        m.shortDescription = Dummy.randomText(140);
        m.nbConsults = Dummy.randomInt(9999);
        m.save();
        
        // Accounts
        m.addAccount(new TwitterAccount(Dummy.randomScreenName()));
        m.addAccount(new GoogleAccount(Dummy.randomGoogleId()));
        
        // Badges
        final int nbBadges = Dummy.randomInt(Badge.values().length);
        for (int i = 0; i < nbBadges; i++) {
            m.addBadge(Badge.values()[Dummy.randomInt(Badge.values().length)]);
        }

        // Interests
        // Some existing interests
        // Some new interests
        final int nbExistingInterests = Dummy.randomInt(5);
        List<Interest> interests = Interest.findAllOrdered();
        for (int i = 0; i < nbExistingInterests; i++) {
            m.addInterest(interests.get(Dummy.randomInt(interests.size())).name);
        }
        final int nbNewInterests = Dummy.randomInt(10);
        for (int i = 0; i < nbNewInterests; i++) {
            m.addInterest(Dummy.randomString(20));
        }
        
        // Shared links
        final int nbSharedLinks = Dummy.randomInt(5);
        for (int i = 0; i < nbSharedLinks; i++) {
            m.addSharedLink(new SharedLink(Dummy.randomString(25), Dummy.randomURL()));
        }
       
        return m.save();
    }
    
    public static Set<Member> createMembers(int nb) {
        Set<Member> members = new HashSet<Member>(nb);
        for (int i = 0; i < nb; i++) {
                    
            Logger.info("Generating dummy member n°"+(i+1));

            members.add(createMember());
        }
        return members;
    }
    
    public static void generateLinks(int averageLinksPerMember) {
        List<Member> members = Member.findAll();
        for (Member m : members) {

            Logger.info("Generating dummy links for member "+m);

            final int nbLinks = Dummy.randomInt(2*averageLinksPerMember);
            for (int i = 0; i < nbLinks; i++) {
                m.addLink(members.get(Dummy.randomInt(members.size())));
            }
            m.save();
        }
    }
    
    public static void generateSessionComments(int averageCommentsPerMember) {
        List<Member> members = Member.findAll();
        List<Session> sessions = Session.findAll();
        for (Member m : members) {

            Logger.info("Generating dummy session comments for member "+m);

            final int nbComments = Dummy.randomInt(2*averageCommentsPerMember);
            for (int i = 0; i < nbComments; i++) {
                Session s = sessions.get(Dummy.randomInt(sessions.size()));
                s.addComment(new SessionComment(m, s, Dummy.randomText(3000)));
                s.save();
            }
        }
    }
    
    public static void generateArticleComments(int averageCommentsPerMember) {
        List<Member> members = Member.findAll();
        List<Article> articles = Article.findAll();
        for (Member m : members) {

            Logger.info("Generating dummy article comments for member "+m);

            final int nbComments = Dummy.randomInt(2*averageCommentsPerMember);
            for (int i = 0; i < nbComments; i++) {
                Article a = articles.get(Dummy.randomInt(articles.size()));
                a.addComment(new ArticleComment(m, a, Dummy.randomText(3000)));
                a.save();
            }
        }
    }
}
