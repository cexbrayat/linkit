package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import play.db.jpa.Model;

/**
 * General persisted parameter for this application
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class GeneralParameter extends Model {
    
    @Entity
    protected static class PersistedParameter extends Model {

        @Column(updatable = false, unique = true)
        public String entry;    // "key" is a SQL reserved word

        public String value;

        public PersistedParameter(String entry) {
            this.entry = entry;
        }

        protected static PersistedParameter find(final String entry) {
            // TODO CLA add cache management
            return find("entry = ?", entry).first();
        }

        protected static PersistedParameter set(final PersistedParameter param) {
            // TODO CLA add cache management
            return param.save();
        }
    }
    
    public static String get(final String entry) {
        PersistedParameter param = PersistedParameter.find(entry);
        return (param == null) ? null : param.value;
    }

    public static void set(final String entry, final Object value) {
        PersistedParameter param = PersistedParameter.find(entry);
        if (param == null) {
            param = new PersistedParameter(entry);
        }
        param.value = value.toString();
        PersistedParameter.set(param);
    }
}
