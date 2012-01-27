package models.generator;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

/**
 * Generates dummy primitive data.
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class Dummy {
    
    static String randomString(int maxlength) {
        return RandomStringUtils.randomAlphabetic(randomInt(maxlength));
    }
    
    static String randomName(int maxlength) {
        return StringUtils.capitalize(randomString(maxlength));
    }
    
    static private String[] SCREEN_NAMES = {"cedric_exbrayat", "julien_ripault", "agnes_crepet", "FranckDepierre", "romaincouturier", "npelloux", "nmartignole", "romainguy"};
    static String randomScreenName() {
        return SCREEN_NAMES[randomInt(SCREEN_NAMES.length)];
    }
    
    static private String[] GOOGLE_IDS = {"108783492614745057153", "105928763613547015053", "116189330666769817164", "111489000265528223150", "113173388528230315265", "105069688047906196113", "105069688047906196113", "112895425540987915280", "111962077049890418486"};
    static String randomGoogleId() {
        return GOOGLE_IDS[randomInt(GOOGLE_IDS.length)];
    }
    
    static private String[] EMAILS = {"nicolas@touilleur-express.fr", "rguy@google.com", "test@test.fr", "toto@toto.fr", "tata@tata.fr", "steve@apple.com", "bill@microsoft.com"};
    static String randomEmail() {
        return EMAILS[randomInt(EMAILS.length)];
    }
    
    static private String[] URLS = {"http://www.google.com", "https://www.github.com", "http://www.yahoo.fr", "http://www.cast-it.fr", "http://www.hypedrivendev.com", "http://www.gamekult.com", "https://plus.google.com", "http://www.test.fr", "http://www.test.com", "http://www.test.us", "http://www.test.co.uk", "http://www.test.tv"};
    static String randomURL() {
        return URLS[randomInt(URLS.length)];
    }
    
    static private String TEXT = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    static String randomText(int maxlength) {
        final int length = randomInt(maxlength);
        StringBuilder str = new StringBuilder(length);
        while (str.length() < length) {
            str.append(TEXT.substring(0, Math.min(TEXT.length(), length - str.length())));
        }
        return str.toString();
    }

    static int randomInt(int max) {
       return RandomUtils.nextInt(max);
    }
}
