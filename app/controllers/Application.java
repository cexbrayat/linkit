package controllers;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import models.*;
import play.Logger;

import java.util.List;
import java.util.Set;
import models.activity.Activity;
import org.apache.commons.lang.StringUtils;
import play.db.jpa.Transactional;
import play.modules.search.Search;
import play.templates.JavaExtensions;

@Transactional(readOnly = true)
public class Application extends PageController {

    public static void index() {

        // DEV MODE : discard connected user if not found in DB (when you restarted your local dev application with initial data)
        String login = Security.connected();
        if (login != null && Member.findByLogin(login) == null) {
            session.remove("username");
        }

        // Three recent articles
        List<Article> articles = Article.recents(1, 3);
        List<Talk> sessions = Talk.recents(1, 3);
        List<Member> members = Member.recents(1, 14);
        // Unused
        // List<Map> tags = Interest.getCloud();
        render(articles, sessions, members);
    }

    public static void members() {
        // Members ordered by last activity
        Activity.OrderedMembersDTO latestActivities = Activity.findOrderedMembers();
        List<Member> members = latestActivities.getMembers();
        // We may use one day OrderedMembersDTO.getLatestActivityFor(member) to display with member 
        render("Application/list.html", members);
    }

    public static void staff() {
        List<Staff> members = Staff.findAll();
        Collections.shuffle(members);
        render("Application/list.html", members);
    }

    public static void speakers() {
        List<Member> members = Talk.findAllSpeakers();
        Collections.shuffle(members);
        render("Application/list.html", members);
    }

    public static void sponsors() {
        List<Sponsor> sponsors = Sponsor.findAll();
        render(sponsors);
    }

    public static void searchByInterest(String interest) {
        Interest i = Interest.findByName(interest);
        List<Member> members = Collections.emptyList();
        List<Talk> talks = Collections.emptyList();
        List<LightningTalk> lightningtalks = Collections.emptyList();
        String query = interest;
        if (i != null) {
            members = Member.findMembersInterestedIn(i);
            talks = Talk.findLinkedWith(i);
            lightningtalks = LightningTalk.findLinkedWith(i);
        }
        render("Application/search.html", members, talks, lightningtalks, query);
    }

    static class LuceneQueryBuilder {

        private String searchTerm;
        private Set<String> fields = Sets.newHashSet();

        protected static boolean isPhraseQuery(String query) {
            return StringUtils.contains(query, ' ');
        }

        protected static String wrap(String query) {
            query = StringUtils.trim(query);
            // Ignore accents
            query = JavaExtensions.noAccents(query);
            if (isPhraseQuery(query)) {
                return new StringBuilder().append('"').append(query).append('"').toString();
            } else {
                return new StringBuilder(query).append('*') // Wilcarded search is not available on phrase query
                        .toString();
            }
        }

        public LuceneQueryBuilder(final String searchTerm) {
            this.searchTerm = wrap(searchTerm);
        }

        public LuceneQueryBuilder orField(String name) {
            fields.add(name);
            return this;
        }

        public String toQuery() {
            StringBuilder query = new StringBuilder();
            for (Iterator<String> iField = fields.iterator(); iField.hasNext();) {
                query.append(iField.next()).append(":").append(searchTerm);
                if (iField.hasNext()) {
                    query.append(" OR ");
                }
            }
            return query.toString();
        }

        @Override
        public String toString() {
            return toQuery();
        }
    }

    // CLA 10/12/2011 : is this controller method is named find(), main.html template doesn't work...
    public static void search(String query) {

        if (StringUtils.isBlank(query)) {
            Application.index();
        }

        // Cela correspond-il à un intérêt?
        final Interest interest = Interest.findByName(query);
        
        final String articlesQuery = new LuceneQueryBuilder(query).orField(Article.TITLE).orField(Article.HEADLINE).orField(Article.CONTENT).toQuery();
        Logger.debug("Search articles with query : %s", articlesQuery);
        List<Article> articles = Search.search(articlesQuery, Article.class).fetch();

        final String sessionsQuery = new LuceneQueryBuilder(query).orField(Session.TITLE).orField(Session.SUMMARY).orField(Session.DESCRIPTION).toQuery();
        Logger.debug("Search sessions with query : %s", sessionsQuery);
        Set<Talk> uniqueTalks = new HashSet(Search.search(sessionsQuery, Talk.class).<Talk>fetch());
        // FIXME CLA Lucene shouldn't index non valid Session
        uniqueTalks = Sets.filter(uniqueTalks, new Predicate<Session>() {
            public boolean apply(Session s) {
                return s.valid;
            }
        });
        if (interest != null) {
            uniqueTalks.addAll(Talk.findLinkedWith(interest));
        }
        List<Talk> talks = new ArrayList<Talk>(uniqueTalks);
        Collections.sort(talks);
        
        Set<LightningTalk> uniqueLightningtalks = new HashSet<LightningTalk>(Search.search(sessionsQuery, LightningTalk.class).<LightningTalk>fetch());
        if (interest != null) {
            uniqueLightningtalks.addAll(LightningTalk.findLinkedWith(interest));
        }
        List<LightningTalk> lightningtalks = new ArrayList<LightningTalk>(uniqueLightningtalks);
        Collections.sort(lightningtalks);

        final String membersQuery = new LuceneQueryBuilder(query).orField(Member.FIRSTNAME).orField(Member.LASTNAME).orField(Member.COMPANY).orField(Member.SHORTDESCRIPTION).orField(Member.LONGDESCRIPTION).toQuery();
        Logger.debug("Search members with query : %s", membersQuery);
        List<Staff> searchStaff = Search.search(membersQuery, Staff.class).fetch();
        List<Sponsor> searchSponsors = Search.search(membersQuery, Sponsor.class).fetch();
        List<Member> searchMembers = Search.search(membersQuery, Member.class).fetch();
        // Building unordered (unique) set of members
        Set<Member> uniqueMembers = new HashSet<Member>(searchStaff.size() + searchSponsors.size() + searchMembers.size());
        uniqueMembers.addAll(searchSponsors);
        uniqueMembers.addAll(searchStaff);
        uniqueMembers.addAll(searchMembers);
        if (interest != null) {
            uniqueMembers.addAll(Member.findMembersInterestedIn(interest));
        }
        // Building ordered list of unique members
        List<Member> members = new ArrayList<Member>(uniqueMembers);
        Collections.sort(members);

        render(query, articles, talks, lightningtalks, members);
    }
}