package controllers;

import java.util.List;
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
    
    public static final String JSON = "json";

    @Before
    public static void loadDefaultData() {
        List<Sponsor> sponsors = Cache.get(SPONSORS, null);
        if (sponsors == null) {
            sponsors = Sponsor.findAll();
            Cache.add(SPONSORS, sponsors, "2h");
        }
        renderArgs.put(SPONSORS, sponsors);
    }
}
