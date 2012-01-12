package controllers;

import com.google.common.collect.Sets;
import java.util.Iterator;
import models.*;
import play.Logger;

import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import play.modules.search.Search;

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
        List<Member> members = Member.findAll();
        Logger.info(members.size() + " membres");
        render("Application/list.html", members);
    }

    public static void staff() {
        List<Staff> members = Staff.findAll();
        Logger.info(members.size() + " membres du staff");
        render("Application/list.html", members);
    }

    public static void speakers() {
        List<Speaker> members = Speaker.findAll();
        Logger.info(members.size() + " speakers");
        render("Application/list.html", members);
    }

    public static void sponsors() {
        List<Sponsor> members = Sponsor.findAll();
        Logger.info(members.size() + " sponsors");
        render("Application/list.html", members);
    }

    public static void searchByInterest(String interest) {
        List<Member> members = Member.findMembersInterestedIn(interest);
        Logger.info(Member.count() + " membres interested by " + interest);
        List<Session> sessions = Session.findSessionsLinkedWith(interest);
        Logger.info(Session.count() + " session linked with " + interest);
        String query = interest;
        render("Application/search.html", members, sessions, query);
    }

    static class LuceneQueryBuilder {
        private String searchTerm;
        private Set<String> fields = Sets.newHashSet();

        protected static boolean isPhraseQuery(String query) {
            return StringUtils.contains(query, ' ');
        }
        protected static String wrap(String query) {
            query = StringUtils.trim(query);
            if (isPhraseQuery(query)) {
                return new StringBuilder()
                    .append('"')
                    .append(query)
                    .append('"').toString();
            } else {
                return new StringBuilder(query)
                    .append('*')    // Wilcarded search is not available on phrase query
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
            for (Iterator<String> iField = fields.iterator(); iField.hasNext(); ) {
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

        final String articlesQuery = new LuceneQueryBuilder(query)
                .orField(Article.TITLE)
                .orField(Article.HEADLINE)
                .orField(Article.CONTENT)
                .toQuery();
        Logger.debug("Search articles with query : %s", articlesQuery);
        List<Article> articles = Search.search(articlesQuery, Article.class).fetch();

        final String sessionsQuery = new LuceneQueryBuilder(query)
                .orField(Session.TITLE)
                .orField(Session.SUMMARY)
                .orField(Session.DESCRIPTION)
                .toQuery();
        Logger.debug("Search sessions with query : %s", sessionsQuery);
        List<Session> sessions = Search.search(sessionsQuery, Session.class).fetch();

        final String membersQuery = new LuceneQueryBuilder(query)
                .orField(Member.FIRSTNAME)
                .orField(Member.LASTNAME)
                .orField(Member.COMPANY)
                .orField(Member.SHORTDESCRIPTION)
                .orField(Member.LONGDESCRIPTION).toQuery();
        Logger.debug("Search members with query : %s", membersQuery);
        List<Member> members = Search.search(membersQuery, Member.class).fetch();

        Interest interest = Interest.findByName(query);
        
        render("Application/search.html", query, articles, sessions, members);
    }
}