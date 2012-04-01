package models.activity;

import models.Article;
import models.BaseDataUnitTest;
import models.LightningTalk;
import models.Member;
import models.Talk;

/**
 * Abstract class for units tests of {@link Activity} and derived classes.
 * @author Sryl <cyril.lacote@gmail.com>
 */
public abstract class AbstractActivityTest extends BaseDataUnitTest {
        
    /** One alone member (no links, no linker) */
    protected Member member;

    protected Member createMember(final String login) {
        return new Member(login).save();
    }
    
    protected Article createArticle(String title) {
        return new Article(member, title).save();
    }

    protected Talk createTalk(String title) {
        Talk t = new Talk();
        t.title = title;
        return t.save();
    }

    protected LightningTalk createLightningTalk(String title) {
        LightningTalk lt = new LightningTalk();
        lt.title = title;
        return lt.save();
    }

    @Override
    public void setUp() {
        super.setUp();
        member = createMember("toto");
    }

    protected void assertActivity(final Activity activity) {
        assertNotNull(activity);
        assertNotNull(activity.at);
        assertNotNull(activity.getUrl());
    }
}
