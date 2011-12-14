package helpers;

/**
 * Executes a piece of code in a transaction
 * @author Sryl <cyril.lacote@gmail.com>
 */
public interface TransactionCallbackWithoutResult {
    
    /**
     * Statements to execute in a transaction
     */
    public void doInTransaction();
}
