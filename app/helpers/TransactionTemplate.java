package helpers;

import play.Logger;
import play.db.jpa.JPAPlugin;

/**
 * Executes {@link TransactionCallback} in an active and managed transaction.
 * Non read-only by default.
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class TransactionTemplate {
    
    /** Is transaction read-only? */
    private boolean readOnly = false;

    public TransactionTemplate() {
    }

    public TransactionTemplate(boolean readOnly) {
        this.readOnly = readOnly;
    }
    
    /**
     * Executes given callback in a transaction
     * @param callback
     * @return Object returned by template, or null
     */
    public <T> T execute(TransactionCallback callback) {
        
        T result = null;
        try {
            JPAPlugin.startTx(readOnly);
            result = callback.doInTransaction();
            JPAPlugin.closeTx(false);
        } catch (RuntimeException e) {
            Logger.error(e, "Exception in transaction template (ROLLBACK) : %s", e.getMessage());
            throw e;
        } finally {
            JPAPlugin.closeTx(true);
        }
        return result;
    }
    
    /**
     * Executes given callback in a transaction
     * @param callback
     */
    public void execute(TransactionCallbackWithoutResult callback) {
        
        try {
            JPAPlugin.startTx(readOnly);
            callback.doInTransaction();
            JPAPlugin.closeTx(false);
        } catch (RuntimeException e) {
            Logger.error(e, "Exception in transaction template (ROLLBACK) : %s", e.getMessage());
            throw e;
        } finally {
            JPAPlugin.closeTx(true);
        }
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
