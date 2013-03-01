package controllers;

import java.util.Collections;
import java.util.List;

import models.ConferenceEvent;
import models.Sponsor;
import play.cache.Cache;
import play.mvc.Before;
import play.mvc.Controller;

/**
 * Base classe for page controller
 * @author Sryl <cyril.lacote@gmail.com>
 */
public abstract class PageController extends Controller {

    public static final String SPONSORS = "sponsors";

    @Before
    public static void loadDefaultData() {
        List<Sponsor> sponsors = Cache.get(SPONSORS, null);
        if (sponsors == null) {
            sponsors = Sponsor.findOn(ConferenceEvent.CURRENT);
            Cache.add(SPONSORS, sponsors, "2h");
        }
        // CLA 07/04/2012 : display sponsors in random order, before true level management (premium/gold/bronze)
        Collections.shuffle(sponsors);
        renderArgs.put(SPONSORS, sponsors);
    }
}
