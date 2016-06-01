package Persistence;

import AbstractException.AbstractException;

/**
 * @author Gerard
 */
public class PersistenceException extends AbstractException {

    /**
     * Constructora.
     */
    public PersistenceException() {
        super();
    }

    /**
     * Constructora amb missatge. El missatge friendly pren el mateix valor.
     * @param message missatge de l'excepcio
     */
    public PersistenceException(String message) {
        super(message);
    }

    /**
     * Constructora un missatge tecnic i un altre friendly.
     * @param message missatge tecnic de l'excepcio
     * @param friendlyMessage missatge friendly
     */
    public PersistenceException(String message, String friendlyMessage) {
        super(message, friendlyMessage);
    }
}
