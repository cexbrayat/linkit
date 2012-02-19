package models.validation;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTimeZone;
import play.data.validation.Check;

/**
 * Validator for Google+ ID
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class TimeZoneCheck extends Check {
    
    public static final String MESSAGE = "validation.timezone";
    
    @Override
    public boolean isSatisfied(final Object validatedObject, final Object value) {
        
        setMessage(MESSAGE);

        if (value == null) return true;
        if (value instanceof String) {
            String strValue = (String) value;
            try {
                return (DateTimeZone.forID(strValue) != null);
            } catch (IllegalArgumentException ex) {
                return false;
            }
        }
        return false;
    }
}
