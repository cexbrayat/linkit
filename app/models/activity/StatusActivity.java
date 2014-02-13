package models.activity;

import helpers.badge.BadgeComputationContext;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Entity;
import javax.persistence.Lob;
import models.Account;
import models.Badge;
import models.Member;
import models.ProviderType;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.IndexColumn;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import play.Logger;
import play.Play;
import play.mvc.Scope;
import play.templates.TemplateLoader;

/**
 * A status activity : someone ({@link Activity#mentionedMember} posted a status on an external provider ({@link Activity#session}
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class StatusActivity extends Activity {

    @Lob
    public String content;
    public String url;
    
    /** ID of status on external provider */
    @IndexColumn(name="statusId_IDX", nullable=false)
    public String statusId;

    /** Pattern for detecting content dealing with Mix-IT */
    private static final Pattern MIXIT_PATTERN = Pattern.compile(".*\\bmix-?it(?:_lyon)?\\b.*", Pattern.CASE_INSENSITIVE);
    
    private static final int FETCH_PERIOD = Integer.valueOf(Play.configuration.getProperty("linkit.timeline.fetch.period"));

    public static final String MENTION_FORMAT = "$LINKIT${%s}";
    private static final Pattern MENTION_PATTERN = Pattern.compile("\\$LINKIT\\$\\{([^}]+)\\}");
    
    public StatusActivity(Member author, Date at, ProviderType provider, String content, String url, String statusId) {
        super(provider, 3, at);
        this.member = author;
        this.provider = provider;
        this.content = StringUtils.substring(content, 0, 4000);
        this.url = url;
        this.statusId = statusId;
    }

    static public void fetchForAccount(Long accountId) {

        Account account = Account.findById(accountId);
        if (account != null) {

            // FIXME CLA simple API quota management 
            boolean fetch = true;
            if (account.lastFetched != null) {
                DateTime lastFetch = new DateTime(account.lastFetched);
                int delay = Minutes.minutesBetween(lastFetch, new DateTime()).getMinutes();
                if (delay < FETCH_PERIOD) {
                    fetch = false;
                    Logger.debug("Fetch timeline for %s on %s : already done %d minutes ago (< %d configured period)", account.member, account.provider, delay, FETCH_PERIOD);
                }
            }
            if (fetch) {
                Logger.debug("Fetch timeline for %s on %s", account.member, account.provider);
                List<StatusActivity> statuses = account.fetchActivities();
                if (!statuses.isEmpty()) {
                    // Memorizing most recent id
                    Collections.sort(statuses);
                    account.lastStatusId = statuses.get(0).statusId;
                    account.save();

                    account.enhance(statuses);
                }

                for (StatusActivity status : statuses) {
                    boolean add = true;
                    // Google hack : workaround for lack of "since" parameter in API, returning already fetched statuses.
                    if (ProviderType.Google == account.provider) {
                        // We add this status only if we don't have it already in DB
                        long count = StatusActivity.count("provider = ? and statusId = ? ", account.provider, status.statusId);
                        add = (count <= 0l);
                    }
                    if (add) status.save();
                }
            }
        } else {
            Logger.error("StatusActivity.fetchForAccount(), account.id = %d not found", accountId);
        }
    }
    
    public static String buildMentionFor(Member mentionned) {
        return String.format(MENTION_FORMAT, mentionned.login);
    }
        
    protected static String renderMention(Member mentionned, Scope.Session s) {
        Map<String, Object> renderArgs = new HashMap<String, Object>(3);
        renderArgs.put("_arg", mentionned);
        renderArgs.put("session", s);
        renderArgs.put("_isInclude", true);
        return TemplateLoader.load("tags/member.html").render(renderArgs);
    }

    public String getContent(Scope.Session s) {
        StringBuffer message = new StringBuffer();
        Matcher matcher = MENTION_PATTERN.matcher(content);
        while (matcher.find()) {
            final String login = matcher.group(1);
            final Member mentionedMember = Member.findByLogin(login);
            matcher.appendReplacement(message, renderMention(mentionedMember, s));
        }
        matcher.appendTail(message);
        return message.toString();
    }
    
    @Override
    public String getUrl() {
        return url;
    }

    /**
     * @return True if content deals with Mix-IT
     */
    public boolean isAboutMixIT() {
        return MIXIT_PATTERN.matcher(content).matches();
    }
    
    @Override
    protected void computedBadgesForConcernedMembers(BadgeComputationContext context) {
        if (isAboutMixIT()) {
            switch (this.provider) {
                case Twitter :
                    member.addBadge(Badge.Twittos);
                    break;
                case Google :
                    member.addBadge(Badge.Plusoner);
                    break;
            }
            member.save();
        }
    }
}
