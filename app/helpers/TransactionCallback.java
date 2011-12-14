package helpers;

/**
 * Executes a piece of code in a transaction
 * @author Sryl <cyril.lacote@gmail.com>
 */
public interface TransactionCallback {
    
    /**
     * Statements to execute in a transaction
     * @return Object to return, or null
     */
    public <T> T doInTransaction();
}
