package controllers;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import models.Member;
import models.Setting;
import models.Staff;
import models.activity.CommentActivity;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Every("1min")
public class JobCommentNotification extends BaseSinceLastTimeJob {

    public JobCommentNotification() {
        super("CommentNotification");
    }

    @Override
    protected void doJobBetween(Date start, Date end) {

        // Avoid fetching comments since start of the universe.
        // We only want new comments starting now.
        Date realStart = (start == null) ? new Date() : start;

        Logger.debug("Fetching CommentActivity from %s to %s", start, end);

        List<CommentActivity> comments = CommentActivity.between(realStart, end);
        Logger.debug("Found %d activities", comments.size());

        if (!comments.isEmpty()) {
            // Staff people always get notified
            Set<Staff> staff = Sets.newHashSet(Staff.<Staff>findAll());

            for (CommentActivity activity : comments) {

                Logger.debug("Notifying comment of session %s", activity.session);

                Set<Member> notifiableMembers = activity.getNotifiableMembers();
                Set<Member> allNotifiablePeople = Sets.union(staff, notifiableMembers);
                for (Member member : allNotifiablePeople) {
                    Mails.commentNotification(member, activity);
                }
            }
        }
    }
}
