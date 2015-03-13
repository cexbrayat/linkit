package controllers;

import com.google.common.collect.Sets;
import models.Comment;
import models.Member;
import models.Staff;
import play.Logger;
import play.jobs.Every;

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

        List<Comment> comments = Comment.between(realStart, end);
        Logger.debug("Found %d comments", comments.size());

        if (!comments.isEmpty()) {
            // Staff people always get notified
            Set<Staff> staff = Sets.newHashSet(Staff.<Staff>findAll());

            for (Comment comment : comments) {

                Logger.debug("Notifying comment %s", comment);

                Set<Member> notifiableMembers = comment.getNotifiableMembers();
                Set<Member> allNotifiablePeople = Sets.union(staff, notifiableMembers);
                for (Member member : allNotifiablePeople) {
                    Mails.commentNotification(member, comment);
                }
            }
        }
    }
}
