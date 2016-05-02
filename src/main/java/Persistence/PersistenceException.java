package Persistence;

import AbstractException.AbstractException;

/**
 * @author Gerard
 */
public class PersistenceException extends AbstractException {
    public PersistenceException() {
        super();
    }

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, String friendlyMessage) {
        super(message, friendlyMessage);
    }
}
