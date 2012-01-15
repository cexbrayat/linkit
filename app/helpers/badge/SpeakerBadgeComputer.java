package helpers.badge;

import java.util.EnumSet;
import java.util.Set;
import models.Badge;
import models.Member;

/**
 * Computer of {@link Badge#Speaker} and {@link Badge#SpeakerPadawan} badges.
 * @author Sryl <cyril.lacote@gmail.com>
 */
class SpeakerBadgeComputer implements BadgeComputer {

    public Set<Badge> compute(final Member member, BadgeComputationContext context) {
        Set<Badge> badges = EnumSet.noneOf(Badge.class);
        if (member.isSpeaker()) {
            badges.add(Badge.Speaker);
        }
        if (member.isLightningTalkSpeaker()) {
            badges.add(Badge.SpeakerPadawan);
        }
        return badges;
    }
}
