package models.activity;

import java.util.Date;
import models.ProviderType;
import org.junit.*;

/**
 * Unit tests for {@link StatusActivity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class StatusActivityTest extends AbstractActivityTest {

    private StatusActivity createActivity(final String content) {
        return new StatusActivity(null, new Date(), ProviderType.LinkIt, content, "http://www.socialnetwork.com", "1234");
    }
    
    @Test
    public void isAboutMixIT() {
        assertTrue(createActivity("ze fz ef Mix-IT azdazdad").isAboutMixIT());
        assertTrue(createActivity("zefzef MixIT azdazdad").isAboutMixIT());
        assertTrue(createActivity("ezefzef mixit azdazdad").isAboutMixIT());
        assertTrue(createActivity("ezefzef http://www.mix-it.fr azdazdad").isAboutMixIT());
        assertTrue(createActivity("ezefzef #MixIT_Lyon azdazdad").isAboutMixIT());
        assertFalse(createActivity("ezefzefzef mixité ezefzef").isAboutMixIT());
        assertFalse(createActivity("ezefzefzefMixit ezefzef").isAboutMixIT());
        assertFalse(createActivity("ezefzefzef mixitdezefzef").isAboutMixIT());
    }
}
