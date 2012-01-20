package models.validation;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import play.data.validation.Check;

/**
 * Validator for Google+ ID
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class GoogleIDCheck extends Check {

    private static final int LENGHT = 21;
    
    private static final Set<Class> acceptedClasses = new HashSet<Class>();
    static {
        acceptedClasses.add(String.class);
        acceptedClasses.add(Integer.class);
        acceptedClasses.add(Long.class);
    }
    
    public static final String MESSAGE = "validation.googleID";
    
    @Override
    public boolean isSatisfied(final Object validatedObject, final Object value) {
        
        setMessage(MESSAGE);

        if (value == null) return true;
        if (!Iterables.any(acceptedClasses, new Predicate<Class>() {
            public boolean apply(Class t) {
                return t.isAssignableFrom(value.getClass());
            }
        })) return false;
        if (value instanceof String) {
            String strValue = (String) value;
            if (StringUtils.isEmpty(strValue)) return true;
            if (strValue.length() != LENGHT) return false;
            if (!StringUtils.isNumeric(strValue)) return false;
        } else {
            Long lValue = (Long) value;
            if (lValue >= Math.pow(10,LENGHT)) return false;
            if (lValue < Math.pow(10,LENGHT-1)) return false;
        }
        return true;
    }
}
